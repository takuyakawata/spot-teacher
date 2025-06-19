package com.spotteacher.admin.feature.lessonPlan.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.graphql.NonEmptyString
import org.springframework.stereotype.Component

data class UpdateLessonPlanMutationInput(
    val id: ID,
    val title: NonEmptyString?,
    val description: NonEmptyString?,
    val imageUrl: NonEmptyString?,
    val videoUrl: NonEmptyString?,
    val type: LessonType?,
    val location: NonEmptyString?,
    val annualMaxExecutions: Int?
)

@Component
class UpdateLessonPlanMutation : Mutation {
    suspend fun updateLessonPlan(input: UpdateLessonPlanMutationInput): UpdateLessonPlanMutationOutput {
        return UpdateLessonPlanMutationOutput(
            success = true
        )
    }
}
