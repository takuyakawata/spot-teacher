package com.spotteacher.teacher.feature.lessonPlan.infra

import arrow.core.Nel
import arrow.core.nonEmptyListOf
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
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class LessonPlanRepositoryImpl(private val dslContext: TransactionAwareDSLContext): LessonPlanRepository {
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
                val recordId = record.id ?: throw IllegalStateException("Lesson plan ID cannot be null")
                val datesList = getLessonPlanDates(recordId)
                val educations = getLessonPlanEducations(recordId)
                val subjects = getLessonPlanSubjects(recordId)
                val grades = getLessonPlanGrades(recordId)
                record.toEntity(datesList, educations, subjects, grades)
            }
        return lessonPlanFlow.toList()
    }

    override suspend fun findById(id: LessonPlanId): LessonPlan? {
        val record = dslContext.get().nonBlockingFetchOne(
            LESSON_PLANS,
            LESSON_PLANS.ID.eq(id.value)
        ) ?: return null

        val recordId = record.id ?: throw IllegalStateException("Lesson plan ID cannot be null")
        val datesList = getLessonPlanDates(recordId)
        val educations = getLessonPlanEducations(recordId)
        val subjects = getLessonPlanSubjects(recordId)
        val grades = getLessonPlanGrades(recordId)
        return record.toEntity(datesList, educations, subjects, grades)
    }

    override suspend fun create(lessonPlan: LessonPlan): LessonPlan {
        // Create a LessonPlan with the provided values
        val now = LocalDateTime.now()
        val lessonPlanId = dslContext.get().insertInto(
            LESSON_PLANS,
            LESSON_PLANS.COMPANY_ID,
            LESSON_PLANS.TITLE,
            LESSON_PLANS.DESCRIPTION,
            LESSON_PLANS.LOCATION,
            LESSON_PLANS.LESSON_TYPE,
            LESSON_PLANS.ANNUAL_MAX_EXECUTIONS,
            LESSON_PLANS.CREATED_AT,
            LESSON_PLANS.UPDATED_AT
        ).values(
            lessonPlan.companyId.value,
            lessonPlan.title.value,
            lessonPlan.description.value,
            lessonPlan.location.value,
            when (lessonPlan.lessonType) {
                LessonType.ONLINE -> LessonPlansLessonType.ONLINE
                LessonType.OFFLINE -> LessonPlansLessonType.OFFLINE
                LessonType.ONLINE_AND_OFFLINE -> LessonPlansLessonType.ONLINE_AND_OFFLINE
            },
            lessonPlan.annualMaxExecutions.toLong(),
            now,
            now
        ).returning(LESSON_PLANS.ID).awaitFirstOrNull()?.id!!

        // Return the created LessonPlan with the generated ID
        return lessonPlan.copy(id = LessonPlanId(lessonPlanId))
    }

    private suspend fun getLessonPlanDates(lessonPlanId: Long): Nel<LessonPlanDate> {
        val dates = dslContext.get().nonBlockingFetch(
            LESSON_PLAN_DATES,
            LESSON_PLAN_DATES.LESSON_PLAN_ID.eq(lessonPlanId)
        ).map { it.toEntity() }

        return if (dates.isNotEmpty()) {
            nonEmptyListOf(dates.first(), *dates.drop(1).toTypedArray())
        } else {
            // Return a default date if no dates are found
            nonEmptyListOf(LessonPlanDate(1, 1, 12, 31, java.time.LocalTime.of(9, 0), java.time.LocalTime.of(17, 0)))
        }
    }

    private suspend fun getLessonPlanEducations(lessonPlanId: Long): Set<EducationId> {
        return dslContext.get().nonBlockingFetch(
            LESSON_PLANS_EDUCATIONS,
            LESSON_PLANS_EDUCATIONS.LESSON_PLAN_ID.eq(lessonPlanId)
        ).map { EducationId(it.educationId) }.toSet()
    }

    private suspend fun getLessonPlanSubjects(lessonPlanId: Long): Set<Subject> {
        return dslContext.get().nonBlockingFetch(
            LESSON_PLAN_SUBJECTS,
            LESSON_PLAN_SUBJECTS.LESSON_PLAN_ID.eq(lessonPlanId)
        ).map { Subject.valueOf(it.subjectCode) }.toSet()
    }

    private suspend fun getLessonPlanGrades(lessonPlanId: Long): Set<Grade> {
        return dslContext.get().nonBlockingFetch(
            LESSON_PLAN_GRADES,
            LESSON_PLAN_GRADES.LESSON_PLAN_ID.eq(lessonPlanId)
        ).map { Grade.valueOf(it.gradeCode) }.toSet()
    }

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

    private fun LessonPlansRecord.toEntity(
        dates: Nel<LessonPlanDate>,
        educations: Set<EducationId>,
        subjects: Set<Subject>,
        grades: Set<Grade>
    ): LessonPlan {
        return LessonPlan(
            id = LessonPlanId(id!!),
            companyId = CompanyId(companyId),
            images = emptyList(), // Images are handled at the application level
            createdAt = createdAt!!,
            educations = LessonPlanEducations(educations),
            subjects = LessonPlanSubjects(subjects),
            grades = LessonPlanGrades(grades),
            title = LessonPlanTitle(title ?: ""),
            description = LessonPlanDescription(description ?: ""),
            lessonType = lessonType?.let { mapEnumToLessonType(it) } ?: LessonType.OFFLINE,
            location = LessonLocation(location ?: ""),
            annualMaxExecutions = annualMaxExecutions?.toInt() ?: 0,
            lessonPlanDates = dates
        )
    }
}
