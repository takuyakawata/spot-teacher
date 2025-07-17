package com.spotteacher.admin.feature.lessonSchedule.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.admin.feature.lessonReservation.infra.LessonReservationRepositoryImpl
import com.spotteacher.admin.feature.lessonSchedule.domain.DoingLessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleError
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleErrorCode
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.admin.feature.lessonSchedule.domain.ScheduleDate
import com.spotteacher.usecase.UseCase

data class CreateLessonScheduleUseCaseInput(
    val lessonReservationId: LessonReservationId,
    val scheduleDate: ScheduleDate
)

@UseCase
class CreateLessonScheduleUseCase(
    private val lessonReservationRepository: LessonReservationRepository,
    private val lessonScheduleRepository: LessonScheduleRepository,
) {
    suspend fun call(input: CreateLessonScheduleUseCaseInput): Either<LessonScheduleError, Unit> {
        val lessonReservation = lessonReservationRepository.findById(input.lessonReservationId)
            ?:return LessonScheduleError(
                message = "Lesson reservation with ID ${input.lessonReservationId.value} not found.",
                errorCode = LessonScheduleErrorCode.LESSON_RESERVATION_NOT_FOUND,
            ).left()

        val schedule = DoingLessonSchedule.create(
            id = LessonScheduleId(0),
            lessonReservationId = lessonReservation.id,
            reservedSchoolId = lessonReservation.reservedSchoolId,
            reservedTeacherId = lessonReservation.reservedTeacherId,
            scheduleDate = input.scheduleDate,
            educations = lessonReservation.educations,
            subjects = lessonReservation.subjects,
            grades = lessonReservation.grades,
            title = lessonReservation.title,
            description = lessonReservation.description,
            lessonType = lessonReservation.lessonType,
            location = lessonReservation.location
        )
        // eternal
        lessonScheduleRepository.create(schedule)
        return Unit.right()
    }
}