package com.spotteacher.admin.feature.lessonTag.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.usecase.CreateEducationUseCase
import com.spotteacher.graphql.NonEmptyString
import org.springframework.stereotype.Component

sealed interface CreateEducationMutationOutput {
    data class CreateEducationMutationSuccess(val result:Unit) : CreateEducationMutationOutput
    data class CreateEducationMutationError(
        val code: EducationErrorCode,
        val message: String
    ) : CreateEducationMutationOutput
}

@Component
class CreateEducationMutation(
    private val usecase: CreateEducationUseCase
) : Mutation {
    suspend fun createEducation(name: NonEmptyString): CreateEducationMutationOutput {
        val result = usecase.call(EducationName(name.value))

        return result.fold(
            ifLeft = { CreateEducationMutationOutput.CreateEducationMutationError(it.code,it.message) },
            ifRight = { CreateEducationMutationOutput.CreateEducationMutationSuccess(Unit) }
        )
    }
}
