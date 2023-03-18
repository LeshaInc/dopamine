package ru.sigsegv.dopamine.resource.study_group;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sigsegv.dopamine.resource.student.Student;
import ru.sigsegv.dopamine.util.NotFoundException;

import java.util.List;

import static ru.sigsegv.dopamine.generated.Tables.STUDENT;
import static ru.sigsegv.dopamine.generated.Tables.STUDY_GROUP;

@RestController
public class StudyGroupResource {
    private final DSLContext jooq;

    @Autowired
    public StudyGroupResource(DSLContext jOOQDSLContext) {
        jooq = jOOQDSLContext;
    }

    @GetMapping("/study-group/{studyGroupId}")
    public Mono<StudyGroup> studyGroup(@PathVariable int studyGroupId) {
        return Mono.from(jooq.selectFrom(STUDY_GROUP)
                        .where(STUDY_GROUP.ID.eq(studyGroupId)))
                .map(r -> r.into(StudyGroup.class))
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(studyGroup -> Flux.from(jooq.select(STUDENT.ID, STUDENT.FULL_NAME)
                                .from(STUDENT)
                                .where(STUDENT.STUDY_GROUP_ID.eq(studyGroupId))
                                .orderBy(STUDENT.FULL_NAME.asc()))
                        .map(r -> r.into(Student.class))
                        .collectList()
                        .map(studyGroup::withStudents));
    }

    @GetMapping("/study-group")
    public Flux<StudyGroup> studyGroupList() {
        return Flux.from(jooq.selectFrom(STUDY_GROUP)
                        .orderBy(STUDY_GROUP.QUALIFICATION, STUDY_GROUP.GRADE, STUDY_GROUP.FACULTY, STUDY_GROUP.NAME))
                .map(r -> r.into(StudyGroup.class));
    }

    private <T> Mono<List<T>> autocomplete(TableField<?, ?> field, Condition groupBy, Condition orderBy, Class<T> type) {
        return Flux.from(jooq.select(field)
                        .from(STUDY_GROUP)
                        .groupBy(groupBy)
                        .orderBy(orderBy)
                        .limit(50))
                .map(r -> r.into(type))
                .collectList();
    }

    private Mono<List<String>> autocompleteString(TableField<?, ?> field, String value) {
        return autocomplete(field, DSL.condition("{0}", field), DSL.condition("{0} <-> {1}", field, value), String.class);
    }

    @GetMapping("/study-group/autocomplete/grade")
    public Mono<List<Integer>> autocompleteGrade(@RequestParam("value") String value) {
        return autocomplete(STUDY_GROUP.GRADE, DSL.condition("{0}", STUDY_GROUP.GRADE), DSL.condition("{0} <-> {1}", STUDY_GROUP.GRADE.cast(String.class), value), Integer.class);
    }

    @GetMapping("/study-group/autocomplete/name")
    public Mono<List<String>> autocompleteName(@RequestParam("value") String value) {
        return autocompleteString(STUDY_GROUP.NAME, value);
    }

    @GetMapping("/study-group/autocomplete/qualification")
    public Mono<List<String>> autocompleteQualification(@RequestParam("value") String value) {
        return autocompleteString(STUDY_GROUP.QUALIFICATION, value);
    }

    @GetMapping("/study-group/autocomplete/faculty")
    public Mono<List<String>> autocompleteFaculty(@RequestParam("value") String value) {
        return autocomplete(STUDY_GROUP.FACULTY,
                DSL.condition("({0}, {1})", STUDY_GROUP.FACULTY, STUDY_GROUP.FACULTY_SHORT),
                DSL.condition("({0} <-> {2}) + ({1} <-> {2}) * 0.5", STUDY_GROUP.FACULTY, STUDY_GROUP.FACULTY_SHORT, value),
                String.class);
    }
}
