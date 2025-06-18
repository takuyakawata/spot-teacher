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
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
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
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class LessonRepositoryImpl(private val dslContext: TransactionAwareDSLContext): LessonPlanRepository {
    suspend fun create(lessonPlan: DraftLessonPlan): LessonPlan {
        return when (lessonPlan) {
            is PublishedLessonPlan -> createPublishedLessonPlan(lessonPlan)
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
            mapLessonTypeToEnum(lessonPlan.lessonType),
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
                date.startTime.atDate(java.time.LocalDate.now()),
                date.endTime.atDate(java.time.LocalDate.now()),
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
            lessonPlan.title?.value ?: "",
            lessonPlan.description?.value,
            lessonPlan.location?.value,
            lessonPlan.lessonType?.let { mapLessonTypeToEnum(it) } ?: LessonPlansLessonType.ONLINE,
            lessonPlan.annualMaxExecutions?.toLong() ?: 0,
        ).returning(LESSON_PLANS.ID).awaitFirstOrNull()?.id!!

        // Insert lesson plan dates if available
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
                id,
                date.startMonth.toLong(),
                date.startDay.toLong(),
                date.endMonth.toLong(),
                date.endDay.toLong(),
                date.startTime.atDate(java.time.LocalDate.now()),
                date.endTime.atDate(java.time.LocalDate.now()),
            ).awaitLast()
        }

        return lessonPlan.copy(id = LessonPlanId(id))
    }

    override suspend fun createDraft(lessonPlan: DraftLessonPlan): DraftLessonPlan {
        TODO("Not yet implemented")
    }

    override suspend fun update(lessonPlan: LessonPlan) {
        when (lessonPlan) {
            is PublishedLessonPlan -> updatePublishedLessonPlan(lessonPlan)
            is DraftLessonPlan -> updateDraftLessonPlan(lessonPlan)
        }
    }

    override suspend fun updateStatus(lessonPlan: LessonPlan) {
        TODO("Not yet implemented")
    }

    private suspend fun updatePublishedLessonPlan(lessonPlan: PublishedLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.COMPANY_ID, lessonPlan.companyId.value)
            .set(LESSON_PLANS.TITLE, lessonPlan.title.value)
            .set(LESSON_PLANS.DESCRIPTION, lessonPlan.description.value)
            .set(LESSON_PLANS.LOCATION, lessonPlan.location.value)
            .set(LESSON_PLANS.LESSON_TYPE, mapLessonTypeToEnum(lessonPlan.lessonType))
            .set(LESSON_PLANS.ANNUAL_MAX_EXECUTIONS, lessonPlan.annualMaxExecutions.toLong())
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
                date.startTime.atDate(java.time.LocalDate.now()),
                date.endTime.atDate(java.time.LocalDate.now()),
            ).awaitLast()
        }
    }

    private suspend fun updateDraftLessonPlan(lessonPlan: DraftLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.COMPANY_ID, lessonPlan.companyId.value)
            .set(LESSON_PLANS.TITLE, lessonPlan.title?.value ?: "")
            .set(LESSON_PLANS.DESCRIPTION, lessonPlan.description?.value)
            .set(LESSON_PLANS.LOCATION, lessonPlan.location?.value)
            .set(LESSON_PLANS.LESSON_TYPE, lessonPlan.lessonType?.let { mapLessonTypeToEnum(it) } ?: LessonPlansLessonType.ONLINE)
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
                date.startTime.atDate(java.time.LocalDate.now()),
                date.endTime.atDate(java.time.LocalDate.now()),
            ).awaitLast()
        }
    }

    override suspend fun delete(id: LessonPlanId) {
        dslContext.get().deleteFrom(LESSON_PLANS)
            .where(LESSON_PLANS.ID.eq(id.value))
            .awaitLast()
    }

    override suspend fun getAll(): List<LessonPlan> {
        val lessonPlansRecords = dslContext.get().nonBlockingFetch(LESSON_PLANS)
        return lessonPlansRecords.map { record ->
            val dates = getLessonPlanDates(record.id!!)
            record.toEntity(dates)
        }
    }

    override suspend fun findById(id: LessonPlanId): LessonPlan? {
        val record = dslContext.get().nonBlockingFetchOne(
            LESSON_PLANS,
            LESSON_PLANS.ID.eq(id.value)
        ) ?: return null

        val dates = getLessonPlanDates(record.id!!)
        return record.toEntity(dates)
    }

    private suspend fun getLessonPlanDates(lessonPlanId: Long): List<LessonPlanDate> {
        return dslContext.get().nonBlockingFetch(
            LESSON_PLAN_DATES,
            LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlanId)
        ).map { it.toEntity() }
    }

    private fun LessonPlansRecord.toEntity(dates: List<LessonPlanDate>): LessonPlan {
        // For simplicity, we're assuming that if title is empty or null, it's a draft
        return if (title.isNullOrEmpty()) {
            DraftLessonPlan(
                id = LessonPlanId(id!!),
                companyId = CompanyId(companyId!!),
                images = emptyList(), // Images are handled at the application level
                createdAt = createdAt!!,
                title = if (title.isNullOrEmpty()) null else LessonPlanTitle(title),
                description = description?.let { LessonPlanDescription(it) },
                lessonType = mapEnumToLessonType(lessonType!!),
                location = location?.let { LessonLocation(it) },
                annualMaxExecutions = annualMaxExecutions?.toInt(),
                lessonPlanDates = if (dates.isEmpty()) null else nonEmptyListOf(dates[0], *dates.drop(1).toTypedArray())
            )
        } else {
            PublishedLessonPlan(
                id = LessonPlanId(id!!),
                companyId = CompanyId(companyId!!),
                images = emptyList(), // Images are handled at the application level
                createdAt = createdAt!!,
                title = LessonPlanTitle(title!!),
                description = LessonPlanDescription(description ?: ""),
                lessonType = mapEnumToLessonType(lessonType!!),
                location = LessonLocation(location ?: ""),
                annualMaxExecutions = annualMaxExecutions!!.toInt(),
                lessonPlanDates = if (dates.isNotEmpty()) {
                    nonEmptyListOf(dates[0], *dates.drop(1).toTypedArray())
                } else {
                    nonEmptyListOf(
                        LessonPlanDate(
                            startMonth = 1,
                            startDay = 1,
                            endMonth = 12,
                            endDay = 31,
                            startTime = LocalTime.of(9, 0),
                            endTime = LocalTime.of(17, 0)
                        )
                    )
                }
            )
        }
    }

    private fun LessonPlanDatesRecord.toEntity(): LessonPlanDate {
        return LessonPlanDate(
            startMonth = startMonth!!.toInt(),
            startDay = startDay!!.toInt(),
            endMonth = endMonth!!.toInt(),
            endDay = endDay!!.toInt(),
            startTime = startTime!!.toLocalTime(),
            endTime = endTime!!.toLocalTime()
        )
    }

    private fun mapLessonTypeToEnum(lessonType: LessonType): LessonPlansLessonType {
        return when (lessonType) {
            LessonType.ONLINE -> LessonPlansLessonType.ONLINE
            LessonType.OFFLINE -> LessonPlansLessonType.OFFLINE
            LessonType.ONLINE_AND_OFFLINE -> LessonPlansLessonType.ONLINE_AND_OFFLINE
        }
    }

    private fun mapEnumToLessonType(lessonType: LessonPlansLessonType): LessonType {
        return when (lessonType) {
            LessonPlansLessonType.ONLINE -> LessonType.ONLINE
            LessonPlansLessonType.OFFLINE -> LessonType.OFFLINE
            LessonPlansLessonType.ONLINE_AND_OFFLINE -> LessonType.ONLINE_AND_OFFLINE
        }
    }
}
