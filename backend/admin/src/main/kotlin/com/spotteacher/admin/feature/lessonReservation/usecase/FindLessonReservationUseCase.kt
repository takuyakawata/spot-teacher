package com.spotteacher.admin.feature.lessonReservation.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationError
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationErrorCode
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindLessonReservationUseCase(
    private val repository: LessonReservationRepository
) {
    suspend fun call(id: LessonReservationId): Either<LessonReservationError,LessonReservation> {
        val lessonReservation = repository.findById(id)?: return LessonReservationError(
            code = LessonReservationErrorCode.LESSON_RESERVATION_NOT_FOUND,
            message = "Lesson reservation with ID ${id.value} not found"
        ).left()

        return lessonReservation.right()
    }
}