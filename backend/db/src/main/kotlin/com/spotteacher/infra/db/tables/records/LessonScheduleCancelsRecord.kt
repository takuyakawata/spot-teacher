/*
 * This file is generated by jOOQ.
 */
package com.spotteacher.infra.db.tables.records


import com.spotteacher.infra.db.tables.LessonScheduleCancels

import java.time.LocalDateTime

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("warnings")
open class LessonScheduleCancelsRecord private constructor() : UpdatableRecordImpl<LessonScheduleCancelsRecord>(LessonScheduleCancels.LESSON_SCHEDULE_CANCELS) {

    open var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var lessonScheduleId: Long
        set(value): Unit = set(1, value)
        get(): Long = get(1) as Long

    open var reason: String
        set(value): Unit = set(2, value)
        get(): String = get(2) as String

    open var createdAt: LocalDateTime?
        set(value): Unit = set(3, value)
        get(): LocalDateTime? = get(3) as LocalDateTime?

    open var updatedAt: LocalDateTime?
        set(value): Unit = set(4, value)
        get(): LocalDateTime? = get(4) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    /**
     * Create a detached, initialised LessonScheduleCancelsRecord
     */
    constructor(id: Long? = null, lessonScheduleId: Long, reason: String, createdAt: LocalDateTime? = null, updatedAt: LocalDateTime? = null): this() {
        this.id = id
        this.lessonScheduleId = lessonScheduleId
        this.reason = reason
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        resetTouchedOnNotNull()
    }
}
