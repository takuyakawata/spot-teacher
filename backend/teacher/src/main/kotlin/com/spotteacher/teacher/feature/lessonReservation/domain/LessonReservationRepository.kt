package com.spotteacher.teacher.feature.lessonReservation.domain

import com.spotteacher.domain.Pagination
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherId

interface LessonReservationRepository {
    suspend fun create(lessonReservation: LessonReservation):LessonReservation
    suspend fun filterByTeacherAndSchoolId(teacherId: TeacherId, schoolId: SchoolId, pagination: Pagination<LessonReservation>): List<LessonReservation>
}