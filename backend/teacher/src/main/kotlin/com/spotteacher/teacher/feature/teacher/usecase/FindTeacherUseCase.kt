package com.spotteacher.teacher.feature.teacher.usecase

import com.spotteacher.teacher.feature.teacher.domain.Teacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindTeacherUseCase(
    private val teacherRepository: TeacherRepository
) {
    suspend fun findById(id: TeacherId): Teacher? {
        return teacherRepository.findById(id)
    }
}
