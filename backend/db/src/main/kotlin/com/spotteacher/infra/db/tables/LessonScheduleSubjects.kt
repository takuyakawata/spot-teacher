/*
 * This file is generated by jOOQ.
 */
package com.spotteacher.infra.db.tables


import com.spotteacher.infra.db.tables.records.LessonScheduleSubjectsRecord

import kotlin.collections.Collection

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("warnings")
open class LessonScheduleSubjects(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, LessonScheduleSubjectsRecord>?,
    parentPath: InverseForeignKey<out Record, LessonScheduleSubjectsRecord>?,
    aliased: Table<LessonScheduleSubjectsRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<LessonScheduleSubjectsRecord>(
    alias,
    null,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>lesson_schedule_subjects</code>
         */
        val LESSON_SCHEDULE_SUBJECTS: LessonScheduleSubjects = LessonScheduleSubjects()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<LessonScheduleSubjectsRecord> = LessonScheduleSubjectsRecord::class.java

    /**
     * The column <code>lesson_schedule_subjects.lesson_schedule_id</code>.
     */
    val LESSON_SCHEDULE_ID: TableField<LessonScheduleSubjectsRecord, Long?> = createField(DSL.name("lesson_schedule_id"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>lesson_schedule_subjects.subject_code</code>.
     */
    val SUBJECT_CODE: TableField<LessonScheduleSubjectsRecord, String?> = createField(DSL.name("subject_code"), SQLDataType.VARCHAR(50).nullable(false), this, "")

    /**
     * The column <code>lesson_schedule_subjects.display_order</code>.
     */
    val DISPLAY_ORDER: TableField<LessonScheduleSubjectsRecord, Int?> = createField(DSL.name("display_order"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "")

    private constructor(alias: Name, aliased: Table<LessonScheduleSubjectsRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<LessonScheduleSubjectsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<LessonScheduleSubjectsRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>lesson_schedule_subjects</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>lesson_schedule_subjects</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>lesson_schedule_subjects</code> table reference
     */
    constructor(): this(DSL.name("lesson_schedule_subjects"), null)
    override fun getPrimaryKey(): UniqueKey<LessonScheduleSubjectsRecord> = Internal.createUniqueKey(LessonScheduleSubjects.LESSON_SCHEDULE_SUBJECTS, DSL.name("KEY_lesson_schedule_subjects_PRIMARY"), arrayOf(LessonScheduleSubjects.LESSON_SCHEDULE_SUBJECTS.LESSON_SCHEDULE_ID, LessonScheduleSubjects.LESSON_SCHEDULE_SUBJECTS.SUBJECT_CODE), true)
    override fun `as`(alias: String): LessonScheduleSubjects = LessonScheduleSubjects(DSL.name(alias), this)
    override fun `as`(alias: Name): LessonScheduleSubjects = LessonScheduleSubjects(alias, this)
    override fun `as`(alias: Table<*>): LessonScheduleSubjects = LessonScheduleSubjects(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): LessonScheduleSubjects = LessonScheduleSubjects(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): LessonScheduleSubjects = LessonScheduleSubjects(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): LessonScheduleSubjects = LessonScheduleSubjects(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): LessonScheduleSubjects = LessonScheduleSubjects(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): LessonScheduleSubjects = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): LessonScheduleSubjects = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): LessonScheduleSubjects = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): LessonScheduleSubjects = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): LessonScheduleSubjects = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): LessonScheduleSubjects = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): LessonScheduleSubjects = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): LessonScheduleSubjects = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): LessonScheduleSubjects = where(DSL.notExists(select))
}
