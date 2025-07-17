package com.spotteacher.admin.feature.lessonReservation.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationErrorCode
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.handler.LessonReservationDate
import com.spotteacher.admin.feature.lessonReservation.handler.LessonReservationType
import com.spotteacher.admin.feature.lessonReservation.handler.toGraphQLID
import com.spotteacher.admin.feature.lessonReservation.usecase.FindLessonReservationUseCase
import com.spotteacher.admin.feature.lessonReservation.usecase.FindPaginatedLessonReservationUseCase
import com.spotteacher.admin.feature.lessonReservation.usecase.FindPaginatedLessonReservationUseCaseInput
import com.spotteacher.domain.SortOrder
import com.spotteacher.graphql.toID
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class LessonReservationsInput(
    val lastReservationId: LastLessonReservationId,
    val limit: Int,
)

data class LastLessonReservationId(
    val id: ID?,
    val order: SortOrder,
)

sealed interface LessonReservationQueryOutput
data class LessonReservationQuerySuccessOutput(
    val lessonReservation: LessonReservationType
) : LessonReservationQueryOutput

data class LessonReservationQueryErrorOutput(
    val code: LessonReservationErrorCode,
    val message: String
) : LessonReservationQueryOutput

@Component
class LessonReservationQuery(
    private val findPaginatedLessonReservationUseCase: FindPaginatedLessonReservationUseCase,
    private val findLessonReservationUseCase: FindLessonReservationUseCase,
): Query {
    suspend fun lessonReservation(id: ID): LessonReservationQueryOutput {
        val result = findLessonReservationUseCase.call(
            id.toDomainId { LessonReservationId(it) }
        )

        return result.fold(
            ifLeft = {
                LessonReservationQueryErrorOutput(
                    code = it.code,
                    message = it.message
                )
            },
            ifRight = {
                LessonReservationQuerySuccessOutput(
                    lessonReservation = mapToLessonReservationType(it)
                )
            }
        )
    }

    suspend fun lessonReservations(input: LessonReservationsInput): List<LessonReservationType> {
        val lastId = input.lastReservationId.let {
            Pair(
                it.id?.let { id -> id.toDomainId { idValue -> LessonReservationId(idValue) } },
                it.order
            )
        }

        val result = findPaginatedLessonReservationUseCase.call(
            FindPaginatedLessonReservationUseCaseInput(
                lastId = lastId,
                limit = input.limit
            )
        )

        return result.map { mapToLessonReservationType(it) }
    }

    private fun mapToLessonReservationType(lessonReservation: LessonReservation): LessonReservationType {
        return LessonReservationType(
            id = lessonReservation.id.toGraphQLID(),
            lessonPlanId = lessonReservation.lessonPlanId,
            reservedSchoolId = lessonReservation.reservedSchoolId,
            teacherId = lessonReservation.reservedTeacherId,
            lessonType = lessonReservation.lessonType,
            location = lessonReservation.location.value,
            reservationDates = lessonReservation.reservationDates.value.map { date ->
                LessonReservationDate(
                    date = date.date,
                    startTime = date.startTime,
                    endTime = date.endTime
                )
            },
            title = lessonReservation.title.value,
            description = lessonReservation.description.value,
            educations = lessonReservation.educations.value.map { it.toID("Education") },
            subjects = lessonReservation.subjects.value.map { ID(it.name) },
            grades = lessonReservation.grades.value.map { ID(it.name) }
        )
    }
}