package ru.sigsegv.dopamine.resource.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ScheduleEntry(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeStart,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeEnd,
        String place, String subjectName, String subjectKind, String teacher) {
}
