package com.spotteacher.teacher.feature.lessonReservation.domain

interface LessonReservationRepository {
    suspend fun create(lessonReservation: LessonReservation):LessonReservation
}