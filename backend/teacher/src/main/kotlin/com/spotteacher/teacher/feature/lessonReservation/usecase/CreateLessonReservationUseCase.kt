package com.spotteacher.teacher.feature.lessonReservation.usecase

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDate
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDates
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation as LessonReservationLocation
import com.spotteacher.teacher.feature.lessonReservation.domain.TeacherId
import com.spotteacher.teacher.feature.school.domain.SchoolId as School
import com.spotteacher.teacher.feature.lessonReservation.handler.LessonReservationType
import com.spotteacher.usecase.UseCase

data class CreateLessonReservationUseCaseInput(
    val teacherId: TeacherId,
    val schoolId: School,
    val lessonPlanId: LessonPlanId,
    val title: LessonReservationTitle,
    val description: LessonReservationTitle,
    val location: LessonReservationLocation,
    val lessonType: LessonReservationType,
    val educations: LessonReservationEducations,
    val subjects: LessonReservationSubjects,
    val grades: LessonReservationGrades,
    )

@UseCase
class CreateLessonReservationUseCase(
    private val lessonReservationRepository: LessonReservationRepository
){
    suspend fun call(input: CreateLessonReservationUseCaseInput): LessonReservation {
        // Create a new lesson reservation using the repository
        val lessonReservation = LessonReservation.create(
            lessonPlanId = input.lessonPlanId,
            reservedSchoolId = input.schoolId,
            reservedTeacherId = input.teacherId,
            educations = input.educations,
            subjects = input.subjects,
            grades = input.grades,
            title = input.title,
            description = LessonReservationDescription(input.description.value),
            reservationDates = LessonReservationDates(nonEmptyListOf(
                LessonReservationDate(
                    date = java.time.LocalDate.now().plusDays(7),
                    startTime = java.time.LocalTime.of(9, 0),
                    endTime = java.time.LocalTime.of(17, 0)
                )
            )),
            reservationLocation = input.location,
            reservationLessonType = input.lessonType.lessonType
        )
        return lessonReservationRepository.create(lessonReservation)
    }
}
