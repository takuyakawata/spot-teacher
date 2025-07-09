package com.spotteacher.teacher.feature.teacher.domain

interface TeacherRepository {
    suspend fun getAll(): List<Teacher>
    suspend fun findById(id: TeacherId): Teacher?
    suspend fun findByUserId(userId: Long): Teacher?
    suspend fun create(teacher: ActiveTeacher): ActiveTeacher
    suspend fun update(teacher: ActiveTeacher)
    suspend fun delete(id: TeacherId)
    suspend fun findBySchoolId(schoolId: Long): List<Teacher>
}
