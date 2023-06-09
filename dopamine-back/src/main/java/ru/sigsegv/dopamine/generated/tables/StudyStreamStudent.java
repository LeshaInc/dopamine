/*
 * This file is generated by jOOQ.
 */
package ru.sigsegv.dopamine.generated.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import ru.sigsegv.dopamine.generated.Keys;
import ru.sigsegv.dopamine.generated.Public;
import ru.sigsegv.dopamine.generated.tables.records.StudyStreamStudentRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StudyStreamStudent extends TableImpl<StudyStreamStudentRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.study_stream_student</code>
     */
    public static final StudyStreamStudent STUDY_STREAM_STUDENT = new StudyStreamStudent();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StudyStreamStudentRecord> getRecordType() {
        return StudyStreamStudentRecord.class;
    }

    /**
     * The column <code>public.study_stream_student.study_stream_id</code>.
     */
    public final TableField<StudyStreamStudentRecord, Integer> STUDY_STREAM_ID = createField(DSL.name("study_stream_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.study_stream_student.student_id</code>.
     */
    public final TableField<StudyStreamStudentRecord, Integer> STUDENT_ID = createField(DSL.name("student_id"), SQLDataType.INTEGER.nullable(false), this, "");

    private StudyStreamStudent(Name alias, Table<StudyStreamStudentRecord> aliased) {
        this(alias, aliased, null);
    }

    private StudyStreamStudent(Name alias, Table<StudyStreamStudentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.study_stream_student</code> table
     * reference
     */
    public StudyStreamStudent(String alias) {
        this(DSL.name(alias), STUDY_STREAM_STUDENT);
    }

    /**
     * Create an aliased <code>public.study_stream_student</code> table
     * reference
     */
    public StudyStreamStudent(Name alias) {
        this(alias, STUDY_STREAM_STUDENT);
    }

    /**
     * Create a <code>public.study_stream_student</code> table reference
     */
    public StudyStreamStudent() {
        this(DSL.name("study_stream_student"), null);
    }

    public <O extends Record> StudyStreamStudent(Table<O> child, ForeignKey<O, StudyStreamStudentRecord> key) {
        super(child, key, STUDY_STREAM_STUDENT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<StudyStreamStudentRecord> getPrimaryKey() {
        return Keys.STUDY_STREAM_STUDENT_PKEY;
    }

    @Override
    public List<ForeignKey<StudyStreamStudentRecord, ?>> getReferences() {
        return Arrays.asList(Keys.STUDY_STREAM_STUDENT__STUDY_STREAM_STUDENT_STUDY_STREAM_ID_FKEY, Keys.STUDY_STREAM_STUDENT__STUDY_STREAM_STUDENT_STUDENT_ID_FKEY);
    }

    private transient StudyStream _studyStream;
    private transient Student _student;

    /**
     * Get the implicit join path to the <code>public.study_stream</code> table.
     */
    public StudyStream studyStream() {
        if (_studyStream == null)
            _studyStream = new StudyStream(this, Keys.STUDY_STREAM_STUDENT__STUDY_STREAM_STUDENT_STUDY_STREAM_ID_FKEY);

        return _studyStream;
    }

    /**
     * Get the implicit join path to the <code>public.student</code> table.
     */
    public Student student() {
        if (_student == null)
            _student = new Student(this, Keys.STUDY_STREAM_STUDENT__STUDY_STREAM_STUDENT_STUDENT_ID_FKEY);

        return _student;
    }

    @Override
    public StudyStreamStudent as(String alias) {
        return new StudyStreamStudent(DSL.name(alias), this);
    }

    @Override
    public StudyStreamStudent as(Name alias) {
        return new StudyStreamStudent(alias, this);
    }

    @Override
    public StudyStreamStudent as(Table<?> alias) {
        return new StudyStreamStudent(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StudyStreamStudent rename(String name) {
        return new StudyStreamStudent(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StudyStreamStudent rename(Name name) {
        return new StudyStreamStudent(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StudyStreamStudent rename(Table<?> name) {
        return new StudyStreamStudent(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super Integer, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super Integer, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
