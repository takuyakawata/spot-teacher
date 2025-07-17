package com.spotteacher.admin.feature.lessonSchedule.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.handler.toGraphQLID
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleErrorCode
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.admin.feature.lessonSchedule.handler.LessonScheduleType
import com.spotteacher.admin.feature.lessonSchedule.handler.toGraphQLID
import com.spotteacher.admin.feature.lessonSchedule.usecase.FindLessonScheduleUseCase
import com.spotteacher.admin.feature.lessonSchedule.usecase.FindPaginatedLessonScheduleUseCase
import com.spotteacher.admin.feature.lessonSchedule.usecase.FindPaginatedLessonScheduleUseCaseInput
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.handler.toGraphQLID
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import com.spotteacher.admin.feature.teacher.handler.toGraphQLID
import com.spotteacher.domain.SortOrder
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

data class LessonSchedulesQueryInput(
    val limit: Int,
    val lastId: LastLessonScheduleId,
)

data class LastLessonScheduleId(
    val id: ID?,
    val order: SortOrder,
)

sealed interface LessonScheduleQueryResult
data class LessonScheduleFound(val result: LessonScheduleType):LessonScheduleQueryResult
data class LessonScheduleNotFound(
    val message: String,
    val code: LessonScheduleErrorCode
):LessonScheduleQueryResult

@Component
class LessonScheduleQuery(
    private val findLessonScheduleUseCase: FindLessonScheduleUseCase,
    private val findPaginatedLessonScheduleUseCase: FindPaginatedLessonScheduleUseCase
) : Query {
    suspend fun lessonSchedules(input: LessonSchedulesQueryInput): List<LessonScheduleType> {
       return findPaginatedLessonScheduleUseCase.call(
           FindPaginatedLessonScheduleUseCaseInput(
               lastId = input.lastId.let {Pair(
                   it.id?.let { id -> id.toDomainId { idValue -> LessonScheduleId(idValue) } },
                   it.order
               ) },
               limit = input.limit,
           )
       ).map { lessonSchedule -> lessonSchedule.toLessonScheduleType() }
    }

    suspend fun lessonSchedule(id: ID): LessonScheduleQueryResult {
      val result = findLessonScheduleUseCase.call(id.toDomainId(::LessonScheduleId))
        return result.fold(
            ifLeft = {
                error -> LessonScheduleNotFound(
                    message = error.message,
                    code = error.errorCode,
                )},
            ifRight = { lessonSchedule -> LessonScheduleFound(
                    result = lessonSchedule.toLessonScheduleType(),
                )}
        )
    }
}

private fun LessonSchedule.toLessonScheduleType() = LessonScheduleType(
    id = this.id.toGraphQLID(),
    lessonReservationId = this.lessonReservationId.toGraphQLID(),
    reservedSchoolId = this.reservedSchoolId.toGraphQLID(),
    teacherId = this.reservedTeacherId.toGraphQLID(),
    lessonType = this.lessonType,
    location = this.location.value,
    title = this.title.value,
    description = this.description.value,
    educations = this.educations.value.toList(),
    subjects = this.subjects.value.toList(),
    date = this.scheduleDate.date,
    startTime = this.scheduleDate.startTime,
    endTime = this.scheduleDate.endTime,
    grades = this.grades.value.toList(),
)
