package com.spotteacher.admin.feature.lessonReservation.domain

interface LessonReservationRepository {
    suspend fun paginated():List<LessonReservation>
}