package com.spotteacher.admin.feature.lessonPlan.infra

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.LessonPlansLessonType
import com.spotteacher.infra.db.tables.LessonPlanDates.Companion.LESSON_PLAN_DATES
import com.spotteacher.infra.db.tables.LessonPlanGrades.Companion.LESSON_PLAN_GRADES
import com.spotteacher.infra.db.tables.LessonPlanSubjects.Companion.LESSON_PLAN_SUBJECTS
import com.spotteacher.infra.db.tables.LessonPlans.Companion.LESSON_PLANS
import com.spotteacher.infra.db.tables.LessonPlansEducations.Companion.LESSON_PLANS_EDUCATIONS
import com.spotteacher.infra.db.tables.records.LessonPlanDatesRecord
import com.spotteacher.infra.db.tables.records.LessonPlansRecord
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class LessonPlanRepositoryImpl(private val dslContext: TransactionAwareDSLContext) : LessonPlanRepository {
    /* createDraft */
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

        lessonPlan.educations.bulkInsertEducations(id)
        lessonPlan.subjects.bulkInsertSubjects(id)
        lessonPlan.grades.bulkInsertGrades(id)

        return lessonPlan.copy(LessonPlanId(id))
    }

    private suspend fun LessonPlanEducations.bulkInsertEducations(id:Long){
        // Insert educations
        this.value.forEachIndexed { index, educationId ->
            dslContext.get().insertInto(
                LESSON_PLANS_EDUCATIONS,
                LESSON_PLANS_EDUCATIONS.LESSON_PLAN_ID,
                LESSON_PLANS_EDUCATIONS.EDUCATION_ID,
                LESSON_PLANS_EDUCATIONS.DISPLAY_ORDER,
            ).values(
                id,
                educationId.value,
                index,
            ).awaitLast()
        }
    }

    private suspend fun LessonPlanSubjects.bulkInsertSubjects(id:Long){
        // Insert subjects
        this.value.forEachIndexed { index, subject ->
            dslContext.get().insertInto(
                LESSON_PLAN_SUBJECTS,
                LESSON_PLAN_SUBJECTS.LESSON_PLAN_ID,
                LESSON_PLAN_SUBJECTS.SUBJECT_CODE,
                LESSON_PLAN_SUBJECTS.DISPLAY_ORDER,
            ).values(
                id,
                subject.name,
                index,
            ).awaitLast()
        }


    }

    private suspend fun LessonPlanGrades.bulkInsertGrades(id:Long){
        // Insert grades
        this.value.forEachIndexed { index, grade ->
            dslContext.get().insertInto(
                LESSON_PLAN_GRADES,
                LESSON_PLAN_GRADES.LESSON_PLAN_ID,
                LESSON_PLAN_GRADES.GRADE_CODE,
                LESSON_PLAN_GRADES.DISPLAY_ORDER,
            ).values(
                id,
                grade.name,
                index,
            ).awaitLast()
        }
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
            .where(LESSON_PLANS.ID.eq(lessonPlan.id.value))
            .awaitLast()

        // Delete existing dates and insert new ones
        dslContext.get().deleteFrom(LESSON_PLAN_DATES)
            .where(LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlan.id.value))
            .awaitLast()

        lessonPlan.lessonPlanDates.map { date ->
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

        // delete and insert
        bulkDeleteEducations(lessonPlan.id.value)
        bulkDeleteSubjects(lessonPlan.id.value)
        bulkDeleteGrades(lessonPlan.id.value)

        lessonPlan.educations.bulkInsertEducations(lessonPlan.id.value)
        lessonPlan.subjects.bulkInsertSubjects(lessonPlan.id.value)
        lessonPlan.grades.bulkInsertGrades(lessonPlan.id.value)
    }

    private suspend fun updateDraftLessonPlan(lessonPlan: DraftLessonPlan) {
        dslContext.get().update(LESSON_PLANS)
            .set(LESSON_PLANS.COMPANY_ID, lessonPlan.companyId.value)
            .set(LESSON_PLANS.TITLE, lessonPlan.title?.value ?: "")
            .set(LESSON_PLANS.DESCRIPTION, lessonPlan.description?.value)
            .set(LESSON_PLANS.LOCATION, lessonPlan.location?.value)
            .set(
                LESSON_PLANS.LESSON_TYPE,
                lessonPlan.lessonType?.let { LessonPlansLessonType.valueOf(it.name)  }
            )
            .set(LESSON_PLANS.ANNUAL_MAX_EXECUTIONS, lessonPlan.annualMaxExecutions?.toLong() ?: 0)
            .where(LESSON_PLANS.ID.eq(lessonPlan.id.value))
            .awaitLast()

        // Delete existing dates
        dslContext.get().deleteFrom(LESSON_PLAN_DATES)
            .where(LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlan.id.value))
            .awaitLast()

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

        bulkDeleteEducations(id.value)
        bulkDeleteSubjects(id.value)
        bulkDeleteGrades(id.value)
    }

    private suspend fun bulkDeleteEducations(id:Long){
        dslContext.get().deleteFrom(LESSON_PLANS_EDUCATIONS)
            .where(LESSON_PLANS_EDUCATIONS.LESSON_PLAN_ID.eq(id))
    }

    private suspend fun bulkDeleteSubjects(id:Long){
        dslContext.get().deleteFrom(LESSON_PLAN_SUBJECTS)
            .where( LESSON_PLAN_SUBJECTS.LESSON_PLAN_ID.eq(id))
    }

    private suspend fun bulkDeleteGrades(id:Long){
        dslContext.get().deleteFrom(LESSON_PLAN_GRADES)
            .where(LESSON_PLAN_GRADES.LESSON_PLAN_ID.eq(id))
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

    /* getPaginated */
    override suspend fun getPaginated(pagination: Pagination<LessonPlan>): List<LessonPlan> {
        val paginationFields = pagination.cursorColumns.mapNotNull {
            LESSON_PLANS.field(it.getDbColumnName())?.let { column ->
                when (it.order) {
                    SortOrder.ASC -> column.asc()
                    SortOrder.DESC -> column.desc()
                } to it.getPrimitiveValue()
            }
        }
        val query = dslContext.get().selectFrom(LESSON_PLANS)
            .orderBy(*paginationFields.map { it.first }.toTypedArray())
            .seek(*paginationFields.map { it.second }.toTypedArray())
            .limit(pagination.limit)

        val lessonPlanFlow = query.asFlow()
            .map { record -> 
                val record = record.into(LessonPlansRecord::class.java)
                val datesList = getLessonPlanDates(record.id!!)
                val dates = toNelOrNull(datesList)
                record.toEntity(dates)
            }
        return lessonPlanFlow.toList()
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
            true -> this.toPublishedLessonPlanEntity(dates!!)
            false -> this.toDraftLessonPlanEntity(dates)
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
            educations = LessonPlanEducations(emptySet()),
            subjects = LessonPlanSubjects(emptySet()),
            grades = LessonPlanGrades(emptySet()),
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
        educations = LessonPlanEducations(emptySet()),
        subjects = LessonPlanSubjects(emptySet()),
        grades = LessonPlanGrades(emptySet()),
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
