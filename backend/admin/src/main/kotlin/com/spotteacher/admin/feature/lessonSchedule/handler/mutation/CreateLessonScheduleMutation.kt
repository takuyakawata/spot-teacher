package com.spotteacher.admin.feature.lessonSchedule.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.lessonSchedule.domain.ScheduleDate
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

@Component
class CreateLessonScheduleMutation: Mutation {
    suspend fun createLessonSchedule(input: CreateLessonScheduleMutationInput) {
        return Unit
    }
}