package com.spotteacher.teacher.feature.teacher.usecase


import com.spotteacher.teacher.feature.teacher.domain.ActiveTeacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import com.spotteacher.teacher.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class CreateTeacherUseCaseInput(
    val userId: Long,
    val schoolId: Long
)

@UseCase
class CreateTeacherUseCase(
    private val teacherRepository: TeacherRepository
) {
    @TransactionCoroutine
    suspend fun call(input: CreateTeacherUseCaseInput): ActiveTeacher {
        // Check if teacher already exists for this user
        val existingTeacher = teacherRepository.findByUserId(input.userId)
        if (existingTeacher != null) {
            throw IllegalArgumentException("Teacher already exists for user ID: ${input.userId}")
        }

        val teacher = ActiveTeacher.create(
            userId = input.userId,
            schoolId = input.schoolId
        )

        return teacherRepository.create(teacher)
    }
}
