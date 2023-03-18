package ru.sigsegv.dopamine.resource.student;

import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sigsegv.dopamine.query.Parser;
import ru.sigsegv.dopamine.query.builder.QueryBuilder;
import ru.sigsegv.dopamine.query.builder.TermBuilder;
import ru.sigsegv.dopamine.resource.study_stream.StudyStream;
import ru.sigsegv.dopamine.util.NotFoundException;
import ru.sigsegv.dopamine.util.Paginated;

import java.util.List;

import static ru.sigsegv.dopamine.generated.Tables.*;

@RestController
public class StudentController {
    private final DSLContext jooq;

    @Autowired
    public StudentController(DSLContext jOOQDSLContext) {
        jooq = jOOQDSLContext;
    }

    @GetMapping("/student/{studentId}")
    public Mono<Student> student(@PathVariable int studentId) {
        return Mono.from(jooq.select(STUDENT.ID, STUDENT.FULL_NAME,
                                DSL.row(STUDY_GROUP.ID, STUDY_GROUP.NAME, STUDY_GROUP.GRADE, STUDY_GROUP.FACULTY,
                                        STUDY_GROUP.FACULTY_SHORT, STUDY_GROUP.QUALIFICATION, STUDY_GROUP.SCHEDULE_ID))
                        .from(STUDENT.join(STUDY_GROUP).on(STUDY_GROUP.ID.eq(STUDENT.STUDY_GROUP_ID)))
                        .where(STUDENT.ID.eq(studentId)))
                .map(r -> r.into(Student.class))
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(student -> {
                    return Flux.from(jooq.select(STUDY_STREAM.ID, STUDY_STREAM.NAME)
                                    .from(STUDY_STREAM_STUDENT.join(STUDY_STREAM).on(STUDY_STREAM.ID.eq(STUDY_STREAM_STUDENT.STUDY_STREAM_ID)))
                                    .where(STUDY_STREAM_STUDENT.STUDENT_ID.eq(studentId))).map(r -> r.into(StudyStream.class))
                            .collectList()
                            .map(student::withStudyStreams);
                });
    }

    @GetMapping("/student")
    public Mono<Paginated<Student>> studentList(@RequestParam("query") String searchQuery,
                                                @RequestParam("sortField") String sortField,
                                                @RequestParam("sortDir") String sortDir,
                                                @RequestParam("page") int page) {
        if (searchQuery == null) searchQuery = "";
        if (sortField == null) sortField = "name";
        if (sortDir == null) sortDir = "asc";

        var searchExpr = Parser.parse(searchQuery);

        TermBuilder groupTerm = (word) -> STUDY_GROUP.NAME.likeIgnoreCase("%" + word + "%");
        TermBuilder nameTerm = (word) -> word.equals("дора")
                ? STUDENT.ID.eq(335045)
                : STUDENT.FULL_NAME.likeIgnoreCase("%" + word + "%");
        TermBuilder combined = (word) -> groupTerm.build(word).or(nameTerm.build(word));

        var queryBuilder = new QueryBuilder(combined);
        queryBuilder.addFunction("фио", nameTerm);
        queryBuilder.addFunction("группа", groupTerm);

        queryBuilder.addFunction("номер", (word) ->
                STUDENT.ID.cast(String.class).eq(word));
        queryBuilder.addFunction("курс", (word) ->
                STUDY_GROUP.GRADE.cast(String.class).eq(word));
        queryBuilder.addFunction("квалификация", (word) ->
                STUDY_GROUP.QUALIFICATION.likeIgnoreCase("%" + word + "%"));
        queryBuilder.addFunction("факультет", (word) ->
                STUDY_GROUP.FACULTY.likeIgnoreCase("%" + word + "%")
                        .or(STUDY_GROUP.FACULTY_SHORT.likeIgnoreCase("%" + word + "%")));

        var orderByField = switch (sortField) {
            case "id" -> STUDENT.ID;
            case "group" -> STUDY_GROUP.NAME;
            case "grade" -> STUDY_GROUP.GRADE;
            default -> STUDENT.FULL_NAME;
        };

        SortField<?> primaryOrderBy = sortDir.equals("asc") ? orderByField.asc() : orderByField.desc();
        SortField<?> secondaryOrderBy = sortDir.equals("asc") ? STUDENT.FULL_NAME.asc() : STUDENT.FULL_NAME.desc();

        var query = jooq.select(STUDENT.ID, STUDENT.FULL_NAME, DSL.row(STUDY_GROUP.ID, STUDY_GROUP.NAME))
                .from(STUDENT.join(STUDY_GROUP).on(STUDY_GROUP.ID.eq(STUDENT.STUDY_GROUP_ID)))
                .where(queryBuilder.build(searchExpr))
                .orderBy(primaryOrderBy, secondaryOrderBy);

        return Paginated.fromQuery(jooq, query, page, 100, Student.class);
    }

    @GetMapping("/student/autocomplete/id")
    public Flux<Integer> autocompleteId(@RequestParam("value") String id) {
        return Flux.from(jooq.select(STUDENT.ID)
                        .from(STUDENT)
                        .orderBy(DSL.condition("{1} <-> {0}", STUDENT.ID.cast(String.class), id))
                        .limit(50))
                .map(r -> r.into(Integer.class));
    }

    @GetMapping("/student/autocomplete/name")
    public Mono<List<String>> autocompleteName(@RequestParam("value") String name) {
        return Flux.from(jooq.select(STUDENT.FULL_NAME)
                        .from(STUDENT)
                        .orderBy(DSL.condition("({1} <-> SPLIT_PART({0}, ' ', 1)) + ({1} <-> SPLIT_PART({0}, ' ', 2)) + ({1} <-> {0}) * 0.25", STUDENT.FULL_NAME, name))
                        .limit(50))
                .map(r -> r.into(String.class))
                .collectList();

    }
}