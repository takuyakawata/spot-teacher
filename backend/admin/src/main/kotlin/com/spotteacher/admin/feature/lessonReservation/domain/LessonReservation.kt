package com.spotteacher.admin.feature.lessonReservation.domain

import arrow.core.Nel
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.util.Identity
import java.time.LocalDate
import java.time.LocalTime

data class LessonReservation(
    val id: LessonReservationId,
    val lessonPlanId: LessonPlanId,
    val reservedSchoolId: SchoolId,
    val reservedTeacherId: TeacherId,
    val educations: LessonReservationEducations,
    val subjects: LessonReservationSubjects,
    val grades: LessonReservationGrades,
    val title: LessonReservationTitle,
    val description: LessonReservationDescription,
    val lessonType: LessonType,
    val location: ReservationLessonLocation,
    val reservationDates: LessonReservationDates,
)

class LessonReservationId(override val value: Long) : Identity<Long>(value)

class TeacherId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class LessonReservationEducations(val value: Set<EducationId>)

@JvmInline
value class LessonReservationSubjects(val value: Set<Subject>)

@JvmInline
value class LessonReservationGrades(val value: Set<Grade>)

@JvmInline
value class LessonReservationTitle(val value: String) {
    companion object {
        const val MAX_LENGTH = 200
    }

    init {
        require(value.length <= MAX_LENGTH) { "Title must be less than $MAX_LENGTH characters" }
    }
}

@JvmInline
value class LessonReservationDescription(val value: String) {
    companion object {
        const val MAX_LENGTH = 1000
    }

    init {
        require(value.length <= MAX_LENGTH) { "Description must be less than $MAX_LENGTH characters" }
    }
}

@JvmInline
value class ReservationLessonLocation(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
    }

    init {
        require(value.length <= MAX_LENGTH) { "Location must be less than $MAX_LENGTH characters" }
    }
}

data class LessonReservationDate(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
){
    init {
        require(startTime.isBefore(endTime)) { "Start time must be before end time" }
    }
}

data class LessonReservationDates(val value: Nel<LessonReservationDate>)

data class LessonReservationError(
    val code: LessonReservationErrorCode,
    val message: String
)

enum class LessonReservationErrorCode {
    LESSON_RESERVATION_NOT_FOUND,
    LESSON_RESERVATION_ALREADY_EXISTS,
    LESSON_RESERVATION_NOT_AVAILABLE, //予約不可
}