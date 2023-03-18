/*
 * This file is generated by jOOQ.
 */
package ru.sigsegv.dopamine.generated.tables;


import java.time.LocalDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function8;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row8;
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
import ru.sigsegv.dopamine.generated.tables.records.ScheduleEntryRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduleEntry extends TableImpl<ScheduleEntryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.schedule_entry</code>
     */
    public static final ScheduleEntry SCHEDULE_ENTRY = new ScheduleEntry();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScheduleEntryRecord> getRecordType() {
        return ScheduleEntryRecord.class;
    }

    /**
     * The column <code>public.schedule_entry.id</code>.
     */
    public final TableField<ScheduleEntryRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.schedule_entry.schedule_id</code>.
     */
    public final TableField<ScheduleEntryRecord, Integer> SCHEDULE_ID = createField(DSL.name("schedule_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.schedule_entry.place</code>.
     */
    public final TableField<ScheduleEntryRecord, String> PLACE = createField(DSL.name("place"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.schedule_entry.subject_kind</code>.
     */
    public final TableField<ScheduleEntryRecord, String> SUBJECT_KIND = createField(DSL.name("subject_kind"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.schedule_entry.subject_name</code>.
     */
    public final TableField<ScheduleEntryRecord, String> SUBJECT_NAME = createField(DSL.name("subject_name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.schedule_entry.teacher</code>.
     */
    public final TableField<ScheduleEntryRecord, String> TEACHER = createField(DSL.name("teacher"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.schedule_entry.time_end</code>.
     */
    public final TableField<ScheduleEntryRecord, LocalDateTime> TIME_END = createField(DSL.name("time_end"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "");

    /**
     * The column <code>public.schedule_entry.time_start</code>.
     */
    public final TableField<ScheduleEntryRecord, LocalDateTime> TIME_START = createField(DSL.name("time_start"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "");

    private ScheduleEntry(Name alias, Table<ScheduleEntryRecord> aliased) {
        this(alias, aliased, null);
    }

    private ScheduleEntry(Name alias, Table<ScheduleEntryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.schedule_entry</code> table reference
     */
    public ScheduleEntry(String alias) {
        this(DSL.name(alias), SCHEDULE_ENTRY);
    }

    /**
     * Create an aliased <code>public.schedule_entry</code> table reference
     */
    public ScheduleEntry(Name alias) {
        this(alias, SCHEDULE_ENTRY);
    }

    /**
     * Create a <code>public.schedule_entry</code> table reference
     */
    public ScheduleEntry() {
        this(DSL.name("schedule_entry"), null);
    }

    public <O extends Record> ScheduleEntry(Table<O> child, ForeignKey<O, ScheduleEntryRecord> key) {
        super(child, key, SCHEDULE_ENTRY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<ScheduleEntryRecord, Integer> getIdentity() {
        return (Identity<ScheduleEntryRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ScheduleEntryRecord> getPrimaryKey() {
        return Keys.SCHEDULE_ENTRY_PKEY;
    }

    @Override
    public ScheduleEntry as(String alias) {
        return new ScheduleEntry(DSL.name(alias), this);
    }

    @Override
    public ScheduleEntry as(Name alias) {
        return new ScheduleEntry(alias, this);
    }

    @Override
    public ScheduleEntry as(Table<?> alias) {
        return new ScheduleEntry(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduleEntry rename(String name) {
        return new ScheduleEntry(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduleEntry rename(Name name) {
        return new ScheduleEntry(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduleEntry rename(Table<?> name) {
        return new ScheduleEntry(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super Integer, ? super Integer, ? super String, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super Integer, ? super Integer, ? super String, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
