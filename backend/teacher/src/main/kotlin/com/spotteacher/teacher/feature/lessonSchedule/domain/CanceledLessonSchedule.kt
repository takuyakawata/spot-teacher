package com.spotteacher.teacher.feature.lessonSchedule.domain

import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation

data class CanceledLessonSchedule(
    override val id: LessonScheduleId,
    override val lessonReservationId: LessonReservationId,
    override val reservedSchoolId: com.spotteacher.teacher.feature.school.domain.SchoolId,
    override val reservedTeacherId: com.spotteacher.teacher.feature.teacher.domain.TeacherId,
    override val scheduleDate:ScheduleDate,
    override val educations: LessonReservationEducations,
    override val subjects: LessonReservationSubjects,
    override val grades: LessonReservationGrades,
    override val title: LessonReservationTitle,
    override val description: LessonReservationDescription,
    override val lessonType: LessonType,
    override val location: ReservationLessonLocation,
    val cancelReason:LessonScheduleCancelReason,
):LessonSchedule

@JvmInline
value class LessonScheduleCancelReason(
    val value: String
){
    companion object{
        const val MAX_LENGTH = 1000
    }
    init {
        require(value.length <= MAX_LENGTH){"Reason must be less than $MAX_LENGTH characters"}
    }
}