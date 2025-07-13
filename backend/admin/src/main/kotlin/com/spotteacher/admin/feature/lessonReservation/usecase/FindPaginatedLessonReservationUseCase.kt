package com.spotteacher.admin.feature.lessonReservation.usecase

import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindPaginatedLessonReservationUseCase(
    private val repository: LessonReservationRepository
) {
    suspend fun call(id: LessonReservationId):Result<LessonReservation>{
        TODO()
    }
}