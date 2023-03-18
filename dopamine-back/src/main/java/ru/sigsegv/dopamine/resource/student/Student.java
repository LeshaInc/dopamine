package ru.sigsegv.dopamine.resource.student;

import ru.sigsegv.dopamine.resource.study_group.StudyGroup;
import ru.sigsegv.dopamine.resource.study_stream.StudyStream;

import java.util.List;

public record Student(int id, String fullName, StudyGroup studyGroup, List<StudyStream> studyStreams) {
    public Student(int id, String fullName, StudyGroup studyGroup) {
        this(id, fullName, studyGroup, null);
    }

    public Student(int id, String fullName) {
        this(id, fullName, null);
    }

    public Student withStudyStreams(List<StudyStream> studyStreams) {
        return new Student(id, fullName, studyGroup, studyStreams);
    }
}
