package com.spotteacher.teacher.feature.teacher.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.domain.EmailAddress
import com.spotteacher.graphql.NonEmptyString
import com.spotteacher.graphql.toDomainId
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherName
import com.spotteacher.teacher.feature.teacher.handler.TeacherType
import com.spotteacher.teacher.feature.teacher.usecase.CreateTeacherUseCase
import com.spotteacher.teacher.feature.teacher.usecase.CreateTeacherUseCaseInput
import org.springframework.stereotype.Component

data class CreateTeacherMutationInput(
    val schoolId: ID,
    val firstName: NonEmptyString,
    val lastName: NonEmptyString,
    val email: NonEmptyString,

)

@Component
class CreateTeacherMutation(
    private val createTeacherUseCase: CreateTeacherUseCase
) : Mutation {
    suspend fun createTeacher(input: CreateTeacherMutationInput): TeacherType {
        val useCaseInput = CreateTeacherUseCaseInput(
            schoolId = input.schoolId.toDomainId(::SchoolId),
            firstName = TeacherName(input.firstName.value),
            lastName = TeacherName(input.lastName.value),
            email = EmailAddress(input.email.value)
        )

        val teacher = createTeacherUseCase.call(useCaseInput)

        return TeacherType(
            id = ID(teacher.id.value.toString()),
            schoolId = teacher.schoolId,
            firstName = teacher.firstName.value,
            lastName = teacher.lastName.value,
            email = teacher.email.value,
        )
    }
}
