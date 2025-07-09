package com.spotteacher.teacher.feature.teacher.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.teacher.feature.teacher.handler.TeacherType
import com.spotteacher.teacher.feature.teacher.usecase.CreateTeacherUseCase
import com.spotteacher.teacher.feature.teacher.usecase.CreateTeacherUseCaseInput
import org.springframework.stereotype.Component

data class CreateTeacherMutationInput(
    val userId: Long,
    val schoolId: Long
)

@Component
class CreateTeacherMutation(
    private val createTeacherUseCase: CreateTeacherUseCase
) : Mutation {
    suspend fun createTeacher(input: CreateTeacherMutationInput): TeacherType {
        val useCaseInput = CreateTeacherUseCaseInput(
            userId = input.userId,
            schoolId = input.schoolId
        )

        val teacher = createTeacherUseCase.call(useCaseInput)

        return TeacherType(
            id = ID(teacher.id.value.toString()),
            userId = teacher.userId,
            schoolId = teacher.schoolId
        )
    }
}
