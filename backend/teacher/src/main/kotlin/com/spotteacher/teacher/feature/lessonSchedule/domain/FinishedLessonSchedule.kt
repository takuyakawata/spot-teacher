package com.spotteacher.teacher.feature.lessonSchedule.domain

import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherId

data class FinishedLessonSchedule(
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
    val reportMessage: FinishedReport,
):LessonSchedule {

}

data class FinishedReport(
    val educationLevel: EducationLevel,
    val twiceReservation: TwiceReservation,
    val message: Message
)

sealed interface FinishedReportType{
    val name :FinishedReportTypeName
}

data class EducationLevel(
    override val name: FinishedReportTypeName = FinishedReportTypeName.EDUCATION_LEVEL,
    val rating:FourStepRating
):FinishedReportType

data class TwiceReservation(
    override val name: FinishedReportTypeName = FinishedReportTypeName.TWICE_RESERVATION,
    val rating:FourStepRating
):FinishedReportType

data class Message(
    override val name: FinishedReportTypeName = FinishedReportTypeName.MESSAGE,
    val comment: FinishedReportComment
):FinishedReportType

//アンケート項目の名前
enum class FinishedReportTypeName(val value:String){
    EDUCATION_LEVEL("内容に満足しましたか？"),
    TWICE_RESERVATION("もう一度申し込みたいと思いましたか？"),
    MESSAGE("メッセージをお願いします！")
}

@JvmInline
value class FourStepRating(val value: Int) {
    companion object {
        const val MIN_VALUE = 1
        const val MAX_VALUE = 4
    }
    init {
        require(value in MIN_VALUE..MAX_VALUE) {"Rating must be between $MIN_VALUE and $MAX_VALUE"}
    }
}

@JvmInline
value class FinishedReportComment(val value:String){
    companion object{
        const val MAX_LENGTH = 1000
    }
    init {
        require(value.length <= MAX_LENGTH){"Message must be less than $MAX_LENGTH characters"}
    }
}