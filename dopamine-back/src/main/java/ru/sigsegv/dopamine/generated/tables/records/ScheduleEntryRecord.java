/*
 * This file is generated by jOOQ.
 */
package ru.sigsegv.dopamine.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;

import ru.sigsegv.dopamine.generated.tables.ScheduleEntry;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduleEntryRecord extends UpdatableRecordImpl<ScheduleEntryRecord> implements Record8<Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.schedule_entry.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.schedule_entry.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.schedule_entry.schedule_id</code>.
     */
    public void setScheduleId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.schedule_entry.schedule_id</code>.
     */
    public Integer getScheduleId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.schedule_entry.place</code>.
     */
    public void setPlace(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.schedule_entry.place</code>.
     */
    public String getPlace() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.schedule_entry.subject_kind</code>.
     */
    public void setSubjectKind(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.schedule_entry.subject_kind</code>.
     */
    public String getSubjectKind() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.schedule_entry.subject_name</code>.
     */
    public void setSubjectName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.schedule_entry.subject_name</code>.
     */
    public String getSubjectName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.schedule_entry.teacher</code>.
     */
    public void setTeacher(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.schedule_entry.teacher</code>.
     */
    public String getTeacher() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.schedule_entry.time_end</code>.
     */
    public void setTimeEnd(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.schedule_entry.time_end</code>.
     */
    public LocalDateTime getTimeEnd() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>public.schedule_entry.time_start</code>.
     */
    public void setTimeStart(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.schedule_entry.time_start</code>.
     */
    public LocalDateTime getTimeStart() {
        return (LocalDateTime) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return ScheduleEntry.SCHEDULE_ENTRY.ID;
    }

    @Override
    public Field<Integer> field2() {
        return ScheduleEntry.SCHEDULE_ENTRY.SCHEDULE_ID;
    }

    @Override
    public Field<String> field3() {
        return ScheduleEntry.SCHEDULE_ENTRY.PLACE;
    }

    @Override
    public Field<String> field4() {
        return ScheduleEntry.SCHEDULE_ENTRY.SUBJECT_KIND;
    }

    @Override
    public Field<String> field5() {
        return ScheduleEntry.SCHEDULE_ENTRY.SUBJECT_NAME;
    }

    @Override
    public Field<String> field6() {
        return ScheduleEntry.SCHEDULE_ENTRY.TEACHER;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return ScheduleEntry.SCHEDULE_ENTRY.TIME_END;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return ScheduleEntry.SCHEDULE_ENTRY.TIME_START;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getScheduleId();
    }

    @Override
    public String component3() {
        return getPlace();
    }

    @Override
    public String component4() {
        return getSubjectKind();
    }

    @Override
    public String component5() {
        return getSubjectName();
    }

    @Override
    public String component6() {
        return getTeacher();
    }

    @Override
    public LocalDateTime component7() {
        return getTimeEnd();
    }

    @Override
    public LocalDateTime component8() {
        return getTimeStart();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getScheduleId();
    }

    @Override
    public String value3() {
        return getPlace();
    }

    @Override
    public String value4() {
        return getSubjectKind();
    }

    @Override
    public String value5() {
        return getSubjectName();
    }

    @Override
    public String value6() {
        return getTeacher();
    }

    @Override
    public LocalDateTime value7() {
        return getTimeEnd();
    }

    @Override
    public LocalDateTime value8() {
        return getTimeStart();
    }

    @Override
    public ScheduleEntryRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value2(Integer value) {
        setScheduleId(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value3(String value) {
        setPlace(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value4(String value) {
        setSubjectKind(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value5(String value) {
        setSubjectName(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value6(String value) {
        setTeacher(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value7(LocalDateTime value) {
        setTimeEnd(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord value8(LocalDateTime value) {
        setTimeStart(value);
        return this;
    }

    @Override
    public ScheduleEntryRecord values(Integer value1, Integer value2, String value3, String value4, String value5, String value6, LocalDateTime value7, LocalDateTime value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScheduleEntryRecord
     */
    public ScheduleEntryRecord() {
        super(ScheduleEntry.SCHEDULE_ENTRY);
    }

    /**
     * Create a detached, initialised ScheduleEntryRecord
     */
    public ScheduleEntryRecord(Integer id, Integer scheduleId, String place, String subjectKind, String subjectName, String teacher, LocalDateTime timeEnd, LocalDateTime timeStart) {
        super(ScheduleEntry.SCHEDULE_ENTRY);

        setId(id);
        setScheduleId(scheduleId);
        setPlace(place);
        setSubjectKind(subjectKind);
        setSubjectName(subjectName);
        setTeacher(teacher);
        setTimeEnd(timeEnd);
        setTimeStart(timeStart);
    }
}