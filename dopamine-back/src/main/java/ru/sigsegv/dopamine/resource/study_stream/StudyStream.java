package ru.sigsegv.dopamine.resource.study_stream;

import ru.sigsegv.dopamine.resource.student.Student;

import java.util.List;

public record StudyStream(int id, Integer scheduleId, String name, List<Student> students) {
    public StudyStream(int id, String name) {
        this(id, null, name, null);
    }

    public StudyStream(int id, Integer scheduleId, String name) {
        this(id, scheduleId, name, null);
    }

    public StudyStream withStudents(List<Student> students) {
        return new StudyStream(id, scheduleId, name, students);
    }
}
