package com.spotteacher.teacher.feature.lessonPlan.domain

import arrow.core.Nel
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import com.spotteacher.util.Identity
import java.time.LocalDateTime
import java.time.LocalTime

data class LessonPlan (
    val id: LessonPlanId,
    val companyId: CompanyId,
    val images: List<UploadFileId>,
    val createdAt: LocalDateTime,
    val educations: LessonPlanEducations,
    val subjects: LessonPlanSubjects,
    val grades: LessonPlanGrades,
    val title: LessonPlanTitle,
    val description: LessonPlanDescription,
    val lessonType: LessonType,
    val location: LessonLocation,
    val annualMaxExecutions: Int,
    val lessonPlanDates: Nel<LessonPlanDate>
)

class LessonPlanId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class LessonPlanDescription(val value: String) {
    companion object {
        const val MAX_LENGTH = 1000
    }
    init {
        require(value.length <= MAX_LENGTH) { "Description must be less than $MAX_LENGTH characters" }
    }
}

@JvmInline
value class LessonPlanTitle(val value: String) {
    companion object {
        const val MAX_LENGTH = 200
    }
    init {
        require(value.length <= MAX_LENGTH) { "Title must be less than $MAX_LENGTH characters" }
    }
}

@JvmInline
value class LessonLocation(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
    }
    init {
        require(value.length <= MAX_LENGTH) { "Location must be less than $MAX_LENGTH characters" }
    }
}

enum class LessonType {
    ONLINE, OFFLINE, ONLINE_AND_OFFLINE
}

data class LessonPlanDate(
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    companion object {
        private const val MAX_DATE = 31
        private const val MINI_DATE = 1
        private const val MAX_MONTH = 12
        private const val MINI_MONTH = 1
    }
    init {
        // TODO 他にロジックありそう？
        require(startMonth in MINI_MONTH..MAX_MONTH) { "Start month must be between $MINI_MONTH and $MAX_MONTH" }
        require(endMonth in MINI_MONTH..MAX_MONTH) { "End month must be between $MINI_MONTH and $MAX_MONTH" }
        require(startMonth <= endMonth) { "Start month must be less than or equal to end month" }
        require(startTime.isBefore(endTime)) { "Start time must be before end time" }
        require(startDay in MINI_DATE..MAX_DATE) { "Start day must be between $MINI_DATE and $MAX_DATE" }
        require(endDay in MINI_DATE..MAX_DATE) { "End day must be between $MINI_DATE and $MAX_DATE" }
    }
}

@JvmInline
value class LessonPlanEducations(val value: Set<EducationId>)

@JvmInline
value class LessonPlanSubjects(val value: Set<Subject>)

@JvmInline
value class LessonPlanGrades(val value: Set<Grade>)

data class LessonPlanError(
    val code: LessonPlanErrorCode,
    val message: String
)

enum class LessonPlanErrorCode {
    LESSON_PLAN_NOT_FOUND,
    LESSON_PLAN_ALREADY_EXISTS,
}
