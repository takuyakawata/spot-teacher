package com.spotteacher.admin.feature.lessonPlan.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.handler.LessonPlanDateInput
import com.spotteacher.admin.feature.lessonPlan.usecase.UpdateLessonPlanUseCase
import com.spotteacher.admin.feature.lessonPlan.usecase.UpdateLessonPlanUseCaseInput
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.graphql.NonEmptyString
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class UpdateLessonPlanMutationInput(
    val id: ID,
    val title: NonEmptyString?,
    val description: NonEmptyString?,
    val images: List<ID>,
    val type: LessonType?,
    val location: NonEmptyString?,
    val annualMaxExecutions: Int?,
    val lessonPlanDates: List<LessonPlanDateInput>?
)

sealed interface UpdateLessonPlanMutationOutput
data class UpdateLessonPlanMutationSuccess(val result: Unit) : UpdateLessonPlanMutationOutput
data class UpdateLessonPlanMutationError(
    val message: String,
    val code: LessonPlanErrorCode
) : UpdateLessonPlanMutationOutput

@Component
class UpdateLessonPlanMutation(
    private val updateLessonPlanUseCase: UpdateLessonPlanUseCase
) : Mutation {
    suspend fun updateLessonPlan(input: UpdateLessonPlanMutationInput): UpdateLessonPlanMutationOutput {
        return updateLessonPlanUseCase.call(
            UpdateLessonPlanUseCaseInput(
                id = input.id.toDomainId { LessonPlanId(it) },
                title = input.title?.let { LessonPlanTitle(it.value) },
                description = input.description?.let { LessonPlanDescription(it.value) },
                lessonType = input.type,
                location = input.location?.let { LessonLocation(it.value) },
                annualMaxExecutions = input.annualMaxExecutions,
                images = input.images.map { it.toDomainId { UploadFileId(it) } }
            )
        ).fold(
            ifLeft = { UpdateLessonPlanMutationError(it.message, it.code) },
            ifRight = { UpdateLessonPlanMutationSuccess(Unit) }
        )
    }
}
