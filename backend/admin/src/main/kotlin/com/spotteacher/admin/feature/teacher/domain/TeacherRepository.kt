package com.spotteacher.admin.feature.teacher.domain

import com.spotteacher.domain.EmailAddress
import org.jetbrains.annotations.TestOnly

interface TeacherRepository {
    suspend fun findById(id: TeacherId): Teacher?
    @TestOnly
    suspend fun create(teacher: Teacher): Teacher
}
