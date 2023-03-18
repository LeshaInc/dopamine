package ru.sigsegv.dopamine.resource.study_stream;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sigsegv.dopamine.resource.student.Student;
import ru.sigsegv.dopamine.util.NotFoundException;

import static ru.sigsegv.dopamine.generated.Tables.*;

@RestController
public class StudyStreamResource {
    private final DSLContext jooq;

    @Autowired
    public StudyStreamResource(DSLContext jOOQDSLContext) {
        jooq = jOOQDSLContext;
    }

    @GetMapping("/study-stream/{studyStreamId}")
    public Mono<StudyStream> studyStream(@PathVariable int studyStreamId) {
        return Mono.from(jooq.select(STUDY_STREAM.ID, STUDY_STREAM.SCHEDULE_ID, STUDY_STREAM.NAME)
                        .from(STUDY_STREAM)
                        .where(STUDY_STREAM.ID.eq(studyStreamId)))
                .map(r -> r.into(StudyStream.class))
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(studyStream -> {
                    return Flux.from(jooq.select(STUDENT.ID, STUDENT.FULL_NAME, DSL.row(STUDY_GROUP.ID, STUDY_GROUP.NAME))
                                    .from(STUDENT
                                            .join(STUDY_STREAM_STUDENT).on(STUDY_STREAM_STUDENT.STUDENT_ID.eq(STUDENT.ID))
                                            .join(STUDY_GROUP).on(STUDY_GROUP.ID.eq(STUDENT.STUDY_GROUP_ID)))
                                    .where(STUDY_STREAM_STUDENT.STUDY_STREAM_ID.eq(studyStreamId))
                                    .orderBy(STUDENT.FULL_NAME.asc()))
                            .map(r -> r.into(Student.class))
                            .collectList()
                            .map(studyStream::withStudents);
                });
    }
}
