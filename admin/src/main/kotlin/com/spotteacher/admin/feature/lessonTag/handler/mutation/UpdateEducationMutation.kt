package com.spotteacher.admin.feature.lessonTag.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.usecase.UpdateEducationUseCase
import com.spotteacher.admin.feature.lessonTag.usecase.UpdateEducationUseCaseInput
import com.spotteacher.graphql.NonEmptyString
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class UpdateEducationMutationInput(
    val id: ID,
    val name: NonEmptyString?,
    val isActive: Boolean?,
    val displayOrder: Int?
)

sealed interface UpdateEducationMutationOutput
data class UpdateEducationMutationSuccess(val result: Unit) : UpdateEducationMutationOutput
data class UpdateEducationMutationError(
    val code: EducationErrorCode,
    val message: String
) : UpdateEducationMutationOutput

@Component
class UpdateEducationMutation(
    private val usecase: UpdateEducationUseCase
) : Mutation {
    suspend fun updateEducation(input: UpdateEducationMutationInput): UpdateEducationMutationOutput {
        val result = usecase.call(
            UpdateEducationUseCaseInput(
                id = input.id.toDomainId { EducationId(it) },
                name = input.name?.let { EducationName(it.value) },
                isActive = input.isActive,
                displayOrder = input.displayOrder
            )
        )

        return result.fold(
            ifLeft = { UpdateEducationMutationError(it.code, it.message) },
            ifRight = { UpdateEducationMutationSuccess(Unit) }
        )
    }
}
