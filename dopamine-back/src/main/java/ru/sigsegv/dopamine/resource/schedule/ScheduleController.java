package ru.sigsegv.dopamine.resource.schedule;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static ru.sigsegv.dopamine.generated.Tables.*;

@RestController
public class ScheduleController {
    private final DSLContext jooq;

    @Autowired
    public ScheduleController(DSLContext jOOQDSLContext) {
        jooq = jOOQDSLContext;
    }

    @GetMapping("/schedule/{scheduleId}")
    public Flux<ScheduleEntry> schedule(@PathVariable int scheduleId,
                                        @RequestParam("start") Long startTimestamp,
                                        @RequestParam("end") Long endTimestamp) {
        var start = convertTime(startTimestamp, 0);
        var end = convertTime(endTimestamp, 999999999999L);

        return Flux.from(jooq.select(SCHEDULE_ENTRY.TIME_START, SCHEDULE_ENTRY.TIME_END,
                                SCHEDULE_ENTRY.PLACE, SCHEDULE_ENTRY.SUBJECT_NAME, SCHEDULE_ENTRY.SUBJECT_KIND, SCHEDULE_ENTRY.TEACHER)
                        .from(SCHEDULE_ENTRY)
                        .where(SCHEDULE_ENTRY.SCHEDULE_ID.eq(scheduleId)
                                .and(SCHEDULE_ENTRY.TIME_START.ge(start))
                                .and(SCHEDULE_ENTRY.TIME_END.le(end)))
                        .orderBy(SCHEDULE_ENTRY.TIME_START.asc()))
                .map(r -> r.into(ScheduleEntry.class));
    }

    @GetMapping("/schedule/student/{studentId}")
    public Mono<List<ScheduleEntry>> studentSchedule(@PathVariable int studentId,
                                                     @RequestParam("start") Long startTimestamp,
                                                     @RequestParam("end") Long endTimestamp) {
        var start = convertTime(startTimestamp, 0);
        var end = convertTime(endTimestamp, 999999999999L);

        return Mono.from(jooq.select(STUDY_GROUP.SCHEDULE_ID)
                        .from(STUDY_GROUP.join(STUDENT).on(STUDENT.STUDY_GROUP_ID.eq(STUDY_GROUP.ID)))
                        .where(STUDENT.ID.eq(studentId)))
                .map(r -> r.into(Integer.class))
                .flatMap(studyGroupScheduleId -> {
                    if (studyGroupScheduleId == null) return Mono.empty();

                    return Flux.from(jooq.select(STUDY_STREAM.SCHEDULE_ID)
                                    .from(STUDY_STREAM_STUDENT.join(STUDY_STREAM).on(STUDY_STREAM.ID.eq(STUDY_STREAM_STUDENT.STUDY_STREAM_ID)))
                                    .where(STUDY_STREAM_STUDENT.STUDENT_ID.eq(studentId)))
                            .map(r -> r.into(Integer.class))
                            .collectList()
                            .flatMap(studyStreamScheduleIds -> {
                                var scheduleIds = Stream.concat(studyStreamScheduleIds.stream(), Stream.of(studyGroupScheduleId)).toList();

                                return Flux.from(jooq.select(SCHEDULE_ENTRY.TIME_START, SCHEDULE_ENTRY.TIME_END,
                                                        SCHEDULE_ENTRY.PLACE, SCHEDULE_ENTRY.SUBJECT_NAME, SCHEDULE_ENTRY.SUBJECT_KIND, SCHEDULE_ENTRY.TEACHER)
                                                .from(SCHEDULE_ENTRY)
                                                .where(SCHEDULE_ENTRY.SCHEDULE_ID.in(scheduleIds)
                                                        .and(SCHEDULE_ENTRY.TIME_START.ge(start))
                                                        .and(SCHEDULE_ENTRY.TIME_END.le(end)))
                                                .orderBy(SCHEDULE_ENTRY.TIME_START.asc()))
                                        .map(r -> r.into(ScheduleEntry.class))
                                        .collectList();
                            });
                });
    }

    private static LocalDateTime convertTime(Long timestamp, long fallback) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp == null ? fallback : timestamp), ZoneId.systemDefault());
    }
}