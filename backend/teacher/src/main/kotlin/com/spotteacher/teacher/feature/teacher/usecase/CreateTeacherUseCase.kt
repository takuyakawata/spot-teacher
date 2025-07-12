package com.spotteacher.teacher.feature.teacher.usecase


import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.Teacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherName
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import com.spotteacher.teacher.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class CreateTeacherUseCaseInput(
    val schoolId: SchoolId,
    val email: EmailAddress,
    val firstName: TeacherName,
    val lastName: TeacherName,
)

@UseCase
class CreateTeacherUseCase(
    private val teacherRepository: TeacherRepository
) {
    @TransactionCoroutine
    suspend fun call(input: CreateTeacherUseCaseInput): Teacher {
        val teacher = Teacher.create(
            schoolId = input.schoolId,
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
        )

        return teacherRepository.create(teacher)
    }
}
