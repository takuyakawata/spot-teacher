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

data class CanceledLessonSchedule(
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