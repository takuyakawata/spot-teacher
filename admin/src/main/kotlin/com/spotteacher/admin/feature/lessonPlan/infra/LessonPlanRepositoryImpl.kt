package com.spotteacher.admin.feature.lessonPlan.infra

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.LessonPlansLessonType
import com.spotteacher.infra.db.tables.LessonPlanDates.Companion.LESSON_PLAN_DATES
import com.spotteacher.infra.db.tables.LessonPlans.Companion.LESSON_PLANS
import com.spotteacher.infra.db.tables.records.LessonPlanDatesRecord
import com.spotteacher.infra.db.tables.records.LessonPlansRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class LessonPlanRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : LessonPlanRepository {
    suspend fun create(lessonPlan: DraftLessonPlan): LessonPlan {
        return when (lessonPlan) {
            is DraftLessonPlan -> createDraftLessonPlan(lessonPlan)
        }
    }

    private suspend fun createPublishedLessonPlan(lessonPlan: PublishedLessonPlan): PublishedLessonPlan {
        val id = dslContext.get().insertInto(
            LESSON_PLANS,
            LESSON_PLANS.COMPANY_ID,
            LESSON_PLANS.TITLE,
            LESSON_PLANS.DESCRIPTION,
            LESSON_PLANS.LOCATION,
            LESSON_PLANS.LESSON_TYPE,
            LESSON_PLANS.ANNUAL_MAX_EXECUTIONS,
        ).values(
            lessonPlan.companyId.value,
            lessonPlan.title.value,
            lessonPlan.description.value,
            lessonPlan.location.value,
            LessonPlansLessonType.valueOf(lessonPlan.lessonType.name),
            lessonPlan.annualMaxExecutions.toLong(),
        ).returning(LESSON_PLANS.ID).awaitFirstOrNull()?.id!!

        // Insert lesson plan dates
        lessonPlan.lessonPlanDates.forEach { date ->
            dslContext.get().insertInto(
                LESSON_PLAN_DATES,
                LESSON_PLAN_DATES.LESSON_PLAN_ID,
                LESSON_PLAN_DATES.START_MONTH,
                LESSON_PLAN_DATES.START_DAY,
                LESSON_PLAN_DATES.END_MONTH,
                LESSON_PLAN_DATES.END_DAY,
                LESSON_PLAN_DATES.START_TIME,
                LESSON_PLAN_DATES.END_TIME,
            ).values(
                id,
                date.startMonth.toLong(),
                date.startDay.toLong(),
                date.endMonth.toLong(),
                date.endDay.toLong(),
                date.startTime.atDate(LocalDate.now()),
                date.endTime.atDate(LocalDate.now()),
            ).awaitLast()
        }

        return lessonPlan.copy(id = LessonPlanId(id))
    }

    private suspend fun createDraftLessonPlan(lessonPlan: DraftLessonPlan): DraftLessonPlan {
        val id = dslContext.get().insertInto(
            LESSON_PLANS,
            LESSON_PLANS.COMPANY_ID,
            LESSON_PLANS.TITLE,
            LESSON_PLANS.DESCRIPTION,
            LESSON_PLANS.LOCATION,
            LESSON_PLANS.LESSON_TYPE,
            LESSON_PLANS.ANNUAL_MAX_EXECUTIONS,
        ).values(
            lessonPlan.companyId.value,
            lessonPlan.title?.value,
            lessonPlan.description?.value,
            lessonPlan.location?.value,
            lessonPlan.lessonType?.let { LessonPlansLessonType.valueOf(it.name) },
            lessonPlan.annualMaxExecutions?.toLong(),
        ).returning(LESSON_PLANS.ID).awaitFirstOrNull()?.id!!

        return lessonPlan.copy(id = LessonPlanId(id))
    }

    override suspend fun createDraft(lessonPlan: DraftLessonPlan): DraftLessonPlan {
        val id = dslContext.get().insertInto(
            LESSON_PLANS,
            LESSON_PLANS.COMPANY_ID,
            LESSON_PLANS.TITLE,
            LESSON_PLANS.DESCRIPTION,
            LESSON_PLANS.LOCATION,
            LESSON_PLANS.LESSON_TYPE,
            LESSON_PLANS.ANNUAL_MAX_EXECUTIONS,
        ).values(
            lessonPlan.companyId.value,
            lessonPlan.title?.value,
            lessonPlan.description?.value,
            lessonPlan.location?.value,
            lessonPlan.lessonType?.let { LessonPlansLessonType.valueOf(it.name) },
            lessonPlan.annualMaxExecutions?.toLong(),
        ).returning(LESSON_PLANS.ID).awaitFirstOrNull()?.id!!

        lessonPlan.lessonPlanDates?.map { date ->
            dslContext.get().insertInto(
                LESSON_PLAN_DATES,
                LESSON_PLAN_DATES.LESSON_PLAN_ID,
                LESSON_PLAN_DATES.START_MONTH,
                LESSON_PLAN_DATES.START_DAY,
                LESSON_PLAN_DATES.END_MONTH,
                LESSON_PLAN_DATES.END_DAY,
                LESSON_PLAN_DATES.START_TIME,
                LESSON_PLAN_DATES.END_TIME,
            ).values(
                id,
                date.startMonth.toLong(),
                date.startDay.toLong(),
                date.endMonth.toLong(),
                date.endDay.toLong(),
                date.startTime.atDate(LocalDate.now()),
                date.endTime.atDate(LocalDate.now()),
            ).awaitLast()
        }

        return lessonPlan.copy(LessonPlanId(id))
    }

    suspend fun createLessonPlanDate(
        id: LessonPlansRecord,
        lessonPlanDates: Nel<LessonPlanDate>
    ): Nel<LessonPlanDatesRecord> {
        TODO()
    }

    /*update*/
    override suspend fun update(lessonPlan: LessonPlan) {
        when (lessonPlan) {
            is PublishedLessonPlan -> updatePublishedLessonPlan(lessonPlan)
            is DraftLessonPlan -> updateDraftLessonPlan(lessonPlan)
        }
    }

    private suspend fun updatePublishedLessonPlan(lessonPlan: PublishedLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.COMPANY_ID, lessonPlan.companyId.value)
            .set(LESSON_PLANS.TITLE, lessonPlan.title.value)
            .set(LESSON_PLANS.DESCRIPTION, lessonPlan.description.value)
            .set(LESSON_PLANS.LOCATION, lessonPlan.location.value)
            .set(LESSON_PLANS.LESSON_TYPE,  LessonPlansLessonType.valueOf(lessonPlan.lessonType.name))
            .set(LESSON_PLANS.ANNUAL_MAX_EXECUTIONS, lessonPlan.annualMaxExecutions.toLong())
            .set(LESSON_PLANS.PUBLISHED, true)
            .where(LESSON_PLANS.ID.eq(lessonPlan.id.value))
            .awaitLast()

        // Delete existing dates and insert new ones
        dslContext.get().deleteFrom(LESSON_PLAN_DATES)
            .where(LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlan.id.value))
            .awaitLast()

        lessonPlan.lessonPlanDates.forEach { date ->
            dslContext.get().insertInto(
                LESSON_PLAN_DATES,
                LESSON_PLAN_DATES.LESSON_PLAN_ID,
                LESSON_PLAN_DATES.START_MONTH,
                LESSON_PLAN_DATES.START_DAY,
                LESSON_PLAN_DATES.END_MONTH,
                LESSON_PLAN_DATES.END_DAY,
                LESSON_PLAN_DATES.START_TIME,
                LESSON_PLAN_DATES.END_TIME,
            ).values(
                lessonPlan.id.value,
                date.startMonth.toLong(),
                date.startDay.toLong(),
                date.endMonth.toLong(),
                date.endDay.toLong(),
                date.startTime.atDate(LocalDate.now()),
                date.endTime.atDate(LocalDate.now()),
            ).awaitLast()
        }
    }

    private suspend fun updateDraftLessonPlan(lessonPlan: DraftLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.COMPANY_ID, lessonPlan.companyId.value)
            .set(LESSON_PLANS.TITLE, lessonPlan.title?.value ?: "")
            .set(LESSON_PLANS.DESCRIPTION, lessonPlan.description?.value)
            .set(LESSON_PLANS.LOCATION, lessonPlan.location?.value)
            .set(
                LESSON_PLANS.LESSON_TYPE,
                lessonPlan.lessonType?.let { LessonPlansLessonType.valueOf(it.name)  } ?: LessonPlansLessonType.ONLINE
            )
            .set(LESSON_PLANS.ANNUAL_MAX_EXECUTIONS, lessonPlan.annualMaxExecutions?.toLong() ?: 0)
            .where(LESSON_PLANS.ID.eq(lessonPlan.id.value))
            .awaitLast()

        // Delete existing dates
        dslContext.get().deleteFrom(LESSON_PLAN_DATES)
            .where(LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlan.id.value))
            .awaitLast()

        // Insert new dates if available
        lessonPlan.lessonPlanDates?.forEach { date ->
            dslContext.get().insertInto(
                LESSON_PLAN_DATES,
                LESSON_PLAN_DATES.LESSON_PLAN_ID,
                LESSON_PLAN_DATES.START_MONTH,
                LESSON_PLAN_DATES.START_DAY,
                LESSON_PLAN_DATES.END_MONTH,
                LESSON_PLAN_DATES.END_DAY,
                LESSON_PLAN_DATES.START_TIME,
                LESSON_PLAN_DATES.END_TIME,
            ).values(
                lessonPlan.id.value,
                date.startMonth.toLong(),
                date.startDay.toLong(),
                date.endMonth.toLong(),
                date.endDay.toLong(),
                date.startTime.atDate(LocalDate.now()),
                date.endTime.atDate(LocalDate.now()),
            ).awaitLast()
        }
    }

    /*updateStatus*/
    override suspend fun updateStatus(lessonPlan: LessonPlan) {
        when (lessonPlan) {
            is PublishedLessonPlan -> convertToPublishedLessonPlan(lessonPlan)
            is DraftLessonPlan -> convertToDraftLessonPlan(lessonPlan)
        }
    }

    private suspend fun convertToPublishedLessonPlan(lessonPlan: PublishedLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.PUBLISHED, true)
            .where(LESSON_PLANS.ID.eq(lessonPlan.id.value))
            .awaitLast()
    }

    private suspend fun convertToDraftLessonPlan(lessonPlan: DraftLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.PUBLISHED, false)
            .where(LESSON_PLANS.ID.eq(lessonPlan.id.value))
            .awaitLast()
    }

    /* delete */
    override suspend fun delete(id: LessonPlanId) {
        dslContext.get().deleteFrom(LESSON_PLANS)
            .where(LESSON_PLANS.ID.eq(id.value))
            .awaitLast()
    }

    /* getAll */
    override suspend fun getAll(): List<LessonPlan> {
        val lessonPlansRecords = dslContext.get().nonBlockingFetch(LESSON_PLANS)
        return lessonPlansRecords.map { record ->
            val datesList = getLessonPlanDates(record.id!!)
            val dates = toNelOrNull(datesList)
            record.toEntity(dates)
        }
    }

    /*findById */
    override suspend fun findById(id: LessonPlanId): LessonPlan? {
        val record = dslContext.get().nonBlockingFetchOne(
            LESSON_PLANS,
            LESSON_PLANS.ID.eq(id.value)
        ) ?: return null

        val datesList = getLessonPlanDates(record.id!!)
        val dates = toNelOrNull(datesList)
        return record.toEntity(dates)
    }

    private suspend fun getLessonPlanDates(lessonPlanId: Long): List<LessonPlanDate> {
        return dslContext.get().nonBlockingFetch(
            LESSON_PLAN_DATES,
            LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlanId)
        ).map { it.toEntity() }
    }

    private fun toNelOrNull(list: List<LessonPlanDate>): Nel<LessonPlanDate>? {
        return if (list.isNotEmpty()) {
            nonEmptyListOf(list.first(), *list.drop(1).toTypedArray())
        } else {
            null
        }
    }

    private suspend fun LessonPlansRecord.toEntity(dates: Nel<LessonPlanDate>?): LessonPlan {
        return when (this.published!!) {
            true -> createPublishedLessonPlan(this.toPublishedLessonPlanEntity(dates!!))
            false -> createDraftLessonPlan(this.toDraftLessonPlanEntity(dates))
        }
    }

    private fun LessonPlansRecord.toDraftLessonPlanEntity(dates: Nel<LessonPlanDate>?): DraftLessonPlan {
        return DraftLessonPlan(
            id = LessonPlanId(id!!),
            companyId = CompanyId(companyId),
            images = emptyList(), // todo Images are handled at the application level
            createdAt = createdAt!!,
            title = title?.let { LessonPlanTitle(it) },
            description = description?.let { LessonPlanDescription(it) },
            lessonType = lessonType?.let { LessonType.valueOf(it.name) },
            location = location?.let { LessonLocation(location!!) },
            lessonPlanDates = dates,
            annualMaxExecutions = annualMaxExecutions?.toInt(),
        )
    }

    private fun LessonPlansRecord.toPublishedLessonPlanEntity(dates: Nel<LessonPlanDate>) = PublishedLessonPlan(
        id = LessonPlanId(id!!),
        companyId = CompanyId(companyId),
        images = emptyList(), // todo Images are handled at the application level
        createdAt = createdAt!!,
        title = LessonPlanTitle(title!!),
        description = LessonPlanDescription(description!!),
        lessonType = mapEnumToLessonType(lessonType!!),
        location = LessonLocation(location!!),
        annualMaxExecutions = annualMaxExecutions!!.toInt(),
        lessonPlanDates = dates,
    )

    private fun LessonPlanDatesRecord.toEntity(): LessonPlanDate {
        return LessonPlanDate(
            startMonth = startMonth.toInt(),
            startDay = startDay.toInt(),
            endMonth = endMonth.toInt(),
            endDay = endDay.toInt(),
            startTime = startTime.toLocalTime(),
            endTime = endTime.toLocalTime()
        )
    }

    private fun mapEnumToLessonType(lessonType: LessonPlansLessonType): LessonType {
        return when (lessonType) {
            LessonPlansLessonType.ONLINE -> LessonType.ONLINE
            LessonPlansLessonType.OFFLINE -> LessonType.OFFLINE
            LessonPlansLessonType.ONLINE_AND_OFFLINE -> LessonType.ONLINE_AND_OFFLINE
        }
    }
}
