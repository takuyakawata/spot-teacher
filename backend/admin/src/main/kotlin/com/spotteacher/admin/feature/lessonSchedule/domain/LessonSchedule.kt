package com.spotteacher.admin.feature.lessonSchedule.domain


import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.admin.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import com.spotteacher.util.Identity
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

sealed interface LessonSchedule{
    val id: LessonScheduleId
    val lessonReservationId: LessonReservationId
    val reservedSchoolId: SchoolId
    val reservedTeacherId: TeacherId
    val scheduleDate:ScheduleDate
    val educations: LessonReservationEducations
    val subjects: LessonReservationSubjects
    val grades: LessonReservationGrades
    val title: LessonReservationTitle
    val description: LessonReservationDescription
    val lessonType: LessonType
    val location: ReservationLessonLocation
}

data class DoingLessonSchedule(
    override val id: LessonScheduleId,
    override val lessonReservationId: LessonReservationId,
    override val reservedSchoolId: SchoolId,
    override val reservedTeacherId: TeacherId,
    override val scheduleDate:ScheduleDate,
    override val educations: LessonReservationEducations,
    override val subjects: LessonReservationSubjects,
    override val grades: LessonReservationGrades,
    override val title: LessonReservationTitle,
    override val description: LessonReservationDescription,
    override val lessonType: LessonType,
    override val location: ReservationLessonLocation,
):LessonSchedule {
    companion object{
        fun create(
            id: LessonScheduleId,
            lessonReservationId: LessonReservationId,
            reservedSchoolId: SchoolId,
            reservedTeacherId: TeacherId,
            scheduleDate:ScheduleDate,
            educations: LessonReservationEducations,
            subjects: LessonReservationSubjects,
            grades: LessonReservationGrades,
            title: LessonReservationTitle,
            description: LessonReservationDescription,
            lessonType: LessonType,
            location: ReservationLessonLocation,
        ) = DoingLessonSchedule(
            id = id,
            lessonReservationId = lessonReservationId,
            reservedSchoolId = reservedSchoolId,
            reservedTeacherId = reservedTeacherId,
            scheduleDate = scheduleDate,
            educations = educations,
            subjects = subjects,
            grades =  grades,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location,
        )
    }
}

class LessonScheduleId(override val value: Long): Identity<Long>(value)

data class ScheduleDate(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: Time,
)
