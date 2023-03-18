package ru.sigsegv.dopamine.resource.study_group;

import ru.sigsegv.dopamine.resource.student.Student;

import java.util.List;

public record StudyGroup(int id, String name, Integer grade, String faculty, String facultyShort, String qualification,
                         Integer scheduleId, List<Student> students) {
    public StudyGroup(int id, String name, Integer grade, String faculty, String facultyShort, String qualification,
                      Integer scheduleId) {
        this(id, name, grade, faculty, facultyShort, qualification, scheduleId, null);
    }

    public StudyGroup(int id, String name) {
        this(id, name, null, null, null, null, null, null);
    }

    public StudyGroup withStudents(List<Student> students) {
        return new StudyGroup(id, name, grade, faculty, facultyShort, qualification, scheduleId, students);
    }
}
