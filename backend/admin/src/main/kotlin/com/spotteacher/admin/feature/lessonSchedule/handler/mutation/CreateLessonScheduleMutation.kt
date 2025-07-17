package com.spotteacher.admin.feature.lessonSchedule.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonScheduleErrorCode
import com.spotteacher.admin.feature.lessonSchedule.domain.ScheduleDate
import com.spotteacher.admin.feature.lessonSchedule.usecase.CreateLessonScheduleUseCase
import com.spotteacher.admin.feature.lessonSchedule.usecase.CreateLessonScheduleUseCaseInput
import org.springframework.stereotype.Component
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

data class CreateLessonScheduleMutationInput(
    val lessonReservationId : ID,
    val scheduleDate:CreateLessonScheduleDateInput,
)

data class CreateLessonScheduleDateInput(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: Time,
)

sealed interface CreateLessonScheduleResult

data class CreateLessonScheduleSuccess(
    val result: Unit
): CreateLessonScheduleResult

data class CreateLessonScheduleError(
    val message: String,
    val errorCode: LessonScheduleErrorCode,
): CreateLessonScheduleResult


@Component
class CreateLessonScheduleMutation(
    private val useCase : CreateLessonScheduleUseCase
): Mutation {
    suspend fun createLessonSchedule(input: CreateLessonScheduleMutationInput): CreateLessonScheduleResult {
       val result = useCase.call(
                CreateLessonScheduleUseCaseInput(
                    lessonReservationId = LessonReservationId(input.lessonReservationId.value.toLong()),
                    scheduleDate = ScheduleDate(
                        date = input.scheduleDate.date,
                        startTime = input.scheduleDate.startTime,
                        endTime = input.scheduleDate.endTime,
                    )
                )
            )

        return result.fold(
            ifLeft = {
                CreateLessonScheduleError(
                    message = it.message,
                    errorCode = it.errorCode,
                )
            },
            ifRight = {
                CreateLessonScheduleSuccess(
                    result = Unit
                )
            }
        )
    }
}