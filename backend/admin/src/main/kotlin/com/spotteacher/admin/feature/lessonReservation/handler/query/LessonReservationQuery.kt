package com.spotteacher.admin.feature.lessonReservation.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.handler.LessonPlanType
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.handler.LessonReservationType
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import org.springframework.stereotype.Component

data class LessonReservationsInput(
    val lastPlanId:LastLessonReservationIdId?,
    val limit: Int,
)

data class LastLessonReservationIdId(
    val id: ID?,
    val order: SortOrder,
)

sealed interface LessonReservationQueryOutput
data class LessonReservationQuerySuccessOutput(
    val lessonPlan: LessonReservationType
) : LessonReservationQueryOutput

data class LessonReservationQueryErrorOutput(
    val code: LessonPlanErrorCode,
    val message: String
) : LessonReservationQueryOutput

@Component
class LessonReservationQuery:Query {
    suspend fun lessonReservation(id: ID):LessonReservationQueryOutput {
        TODO()
    }

    suspend fun lessonReservations(input:LessonReservationsInput):List<LessonReservationType>{
        TODO()
    }
}