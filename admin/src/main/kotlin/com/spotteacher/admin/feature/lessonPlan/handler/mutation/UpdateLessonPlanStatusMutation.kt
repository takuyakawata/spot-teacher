package com.spotteacher.admin.feature.lessonPlan.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.usecase.UpdateStatusLessonPlanUseCase
import com.spotteacher.graphql.toDomainId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

sealed interface UpdateLessonPlanStatusMutationOutput
data class UpdateLessonPlanStatusMutationSuccess(val result: Unit) : UpdateLessonPlanStatusMutationOutput
data class UpdateLessonPlanStatusMutationError(
    val message: String,
    val code: LessonPlanErrorCode
) : UpdateLessonPlanStatusMutationOutput

@Component
class UpdateLessonPlanStatusMutation(
    private val updateStatusLessonPlanUseCase: UpdateStatusLessonPlanUseCase
) : Mutation {
    @PreAuthorize("isAuthenticated()")
    suspend fun updateLessonPlanStatus(id: ID): UpdateLessonPlanStatusMutationOutput {
        return updateStatusLessonPlanUseCase.call(id.toDomainId { LessonPlanId(it) }).fold(
            { error ->
                UpdateLessonPlanStatusMutationError(
                    message = error.message,
                    code = error.code
                )
            },
            { UpdateLessonPlanStatusMutationSuccess(Unit) }
        )
    }
}
