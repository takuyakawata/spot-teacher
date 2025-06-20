package com.spotteacher.admin.feature.lessonTag.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.usecase.DeleteEducationUseCase
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component


sealed interface DeleteEducationMutationOutput
data class DeleteEducationMutationSuccess(val result:Unit) : DeleteEducationMutationOutput
data class DeleteEducationMutationError(
    val code: EducationErrorCode,
    val message: String
) : DeleteEducationMutationOutput

@Component
class DeleteEducationMutation(
    private val usecase: DeleteEducationUseCase
) : Mutation {
    suspend fun deleteEducation(id: ID): DeleteEducationMutationOutput {
        val result = usecase.call(id.toDomainId(::EducationId))

        return result.fold(
            ifLeft = { DeleteEducationMutationError(it.code,it.message) },
            ifRight = { DeleteEducationMutationSuccess(Unit) }
        )
    }
}
