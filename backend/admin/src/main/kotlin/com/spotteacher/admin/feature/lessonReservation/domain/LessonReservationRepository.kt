package com.spotteacher.admin.feature.lessonReservation.domain

import com.spotteacher.domain.Pagination
import org.jetbrains.annotations.TestOnly

interface LessonReservationRepository {
    suspend fun paginated(pagination: Pagination<LessonReservation>):List<LessonReservation>
    suspend fun findById(id: LessonReservationId): LessonReservation?

    @TestOnly
    suspend fun create(lessonReservation: LessonReservation): LessonReservation
}