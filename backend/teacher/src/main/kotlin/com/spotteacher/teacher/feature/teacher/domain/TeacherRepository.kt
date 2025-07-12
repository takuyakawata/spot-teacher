package com.spotteacher.teacher.feature.teacher.domain

import com.spotteacher.domain.EmailAddress

interface TeacherRepository {
    suspend fun findById(id: TeacherId): Teacher?
    suspend fun create(teacher: Teacher): Teacher
    suspend fun findByEmail(email: EmailAddress): Teacher?
}
