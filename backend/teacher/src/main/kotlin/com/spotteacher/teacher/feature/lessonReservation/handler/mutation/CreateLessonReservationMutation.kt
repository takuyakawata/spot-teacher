package com.spotteacher.teacher.feature.lessonReservation.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.teacher.feature.lessonReservation.usecase.CreateLessonReservationUseCase
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import org.springframework.stereotype.Component

data class CreateLessonReservationMutationInput(
    val lessonPlanId: ID,
    val title: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val companyId: ID,
    val educations: List<ID>,
    val subjects: List<com.spotteacher.teacher.feature.lessonTag.domain.Subject>,
    val grades: List<Grade>
)

@Component
class CreateLessonReservationMutation(
    private val useCase: CreateLessonReservationUseCase
) : Mutation {
    suspend fun createLessonReservation() {

    }
}
