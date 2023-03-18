package ru.sigsegv.dopamine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static ru.sigsegv.dopamine.generated.Tables.*;

public class Importer {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");

        var userName = "weblab";
        var password = "pass";
        var url = "jdbc:postgresql://localhost:5432/weblab3";

        var conn = DriverManager.getConnection(url, userName, password);
        var ctx = DSL.using(conn, SQLDialect.POSTGRES);

        var mapper = new ObjectMapper();
        var tree = mapper.readTree(new File("/home/leshainc/Projects/isuscraper/groups.json"));

        for (var group : tree) {
            System.out.println(group.toString());
            ctx.update(STUDY_GROUP)
                    .set(STUDY_GROUP.QUALIFICATION, group.get("qualification").asText())
                    .set(STUDY_GROUP.FACULTY, group.get("faculty").asText())
                    .set(STUDY_GROUP.FACULTY_SHORT, group.get("facultyShort").asText())
                    .set(STUDY_GROUP.GRADE, group.get("grade").asInt())
                    .where(STUDY_GROUP.NAME.eq(group.get("group").asText()))
                    .execute();
        }
    }

    public static void main2(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");

        var userName = "weblab";
        var password = "pass";
        var url = "jdbc:postgresql://localhost:5432/weblab3";

        var conn = DriverManager.getConnection(url, userName, password);
        var ctx = DSL.using(conn, SQLDialect.POSTGRES);

        ctx.deleteFrom(STUDY_STREAM_STUDENT).execute();
        ctx.deleteFrom(STUDENT).execute();
        ctx.deleteFrom(SCHEDULE_ENTRY).execute();
        ctx.deleteFrom(STUDY_GROUP).execute();
        ctx.deleteFrom(STUDY_STREAM).execute();

        var result = ctx.select().from(STUDENT).fetch();

        var scheduleId = 1;

        for (var path : Objects.requireNonNull(new File("/home/leshainc/Projects/isuscraper/groups/").listFiles())) {
            System.out.println(path);
            var mapper = new ObjectMapper();
            var tree = mapper.readTree(path);

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (var sched : tree.get("schedule")) {
                var timeStart = LocalDateTime.parse(sched.get(0).asText(), formatter);
                var timeEnd = LocalDateTime.parse(sched.get(1).asText(), formatter);
                var place = String.join("\n", mapper.convertValue(sched.get(2), new TypeReference<List<String>>() {}));
                var subjectName = sched.get(3).asText();
                var subjectKind = sched.get(4).asText();
                var teacher = sched.get(5).asText();
                ctx.insertInto(SCHEDULE_ENTRY)
                        .set(SCHEDULE_ENTRY.SCHEDULE_ID, scheduleId)
                        .set(SCHEDULE_ENTRY.TIME_START, timeStart)
                        .set(SCHEDULE_ENTRY.TIME_END, timeEnd)
                        .set(SCHEDULE_ENTRY.PLACE, place)
                        .set(SCHEDULE_ENTRY.SUBJECT_NAME, subjectName)
                        .set(SCHEDULE_ENTRY.SUBJECT_KIND, subjectKind)
                        .set(SCHEDULE_ENTRY.TEACHER, teacher)
                        .execute();
            }

            var studyGroupId = ctx.insertInto(STUDY_GROUP)
                    .set(STUDY_GROUP.SCHEDULE_ID, scheduleId)
                    .set(STUDY_GROUP.NAME, tree.get("group").asText())
                    .returningResult(STUDY_GROUP.ID)
                    .fetchOne().value1();

            for (var member : tree.get("members")) {
                ctx.insertInto(STUDENT)
                        .set(STUDENT.ID, Integer.parseInt(member.get(0).asText()))
                        .set(STUDENT.STUDY_GROUP_ID, studyGroupId)
                        .set(STUDENT.FULL_NAME, member.get(1).asText())
                        .execute();
            }

            scheduleId++;
        }

        for (var path : Objects.requireNonNull(new File("/home/leshainc/Projects/isuscraper/streams/").listFiles())) {
            System.out.println(path);
            var mapper = new ObjectMapper();
            var tree = mapper.readTree(path);

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (var sched : tree.get("schedule")) {
                var timeStart = LocalDateTime.parse(sched.get(0).asText(), formatter);
                var timeEnd = LocalDateTime.parse(sched.get(1).asText(), formatter);
                var place = String.join("\n", mapper.convertValue(sched.get(2), new TypeReference<List<String>>() {}));
                var subjectName = sched.get(3).asText();
                var subjectKind = sched.get(4).asText();
                var teacher = sched.get(5).asText();
                ctx.insertInto(SCHEDULE_ENTRY)
                        .set(SCHEDULE_ENTRY.SCHEDULE_ID, scheduleId)
                        .set(SCHEDULE_ENTRY.TIME_START, timeStart)
                        .set(SCHEDULE_ENTRY.TIME_END, timeEnd)
                        .set(SCHEDULE_ENTRY.PLACE, place)
                        .set(SCHEDULE_ENTRY.SUBJECT_NAME, subjectName)
                        .set(SCHEDULE_ENTRY.SUBJECT_KIND, subjectKind)
                        .set(SCHEDULE_ENTRY.TEACHER, teacher)
                        .execute();
            }

            var studyStreamId = ctx.insertInto(STUDY_STREAM)
                    .set(STUDY_STREAM.NAME, tree.get("name").asText())
                    .set(STUDY_STREAM.SCHEDULE_ID, scheduleId)
                    .returningResult(STUDY_STREAM.ID).fetchOne().value1();

            for (var member : tree.get("members")) {
                var studentId = Integer.parseInt(member.get(0).asText());
                if (ctx.selectFrom(STUDENT).where(STUDENT.ID.eq(studentId)).fetchOne() == null) continue;
                ctx.insertInto(STUDY_STREAM_STUDENT)
                        .set(STUDY_STREAM_STUDENT.STUDENT_ID, studentId)
                        .set(STUDY_STREAM_STUDENT.STUDY_STREAM_ID, studyStreamId)
                        .execute();
            }

            scheduleId++;
        }
    }
}