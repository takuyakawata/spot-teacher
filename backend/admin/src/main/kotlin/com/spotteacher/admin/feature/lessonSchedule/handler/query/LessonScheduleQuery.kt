package com.spotteacher.admin.feature.lessonSchedule.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.lessonSchedule.domain.LessonSchedule
import com.spotteacher.admin.feature.lessonSchedule.handler.LessonScheduleType
import com.spotteacher.admin.feature.lessonSchedule.usecase.FindLessonScheduleUseCase
import com.spotteacher.admin.feature.lessonSchedule.usecase.FindPaginatedLessonScheduleUseCase
import org.springframework.stereotype.Component

@Component
class LessonScheduleQuery(
    private val findLessonScheduleUseCase: FindLessonScheduleUseCase,
    private val findPaginatedLessonScheduleUseCase: FindPaginatedLessonScheduleUseCase
) : Query {
    suspend fun lessonSchedules(): List<LessonScheduleType> {
       return findPaginatedLessonScheduleUseCase.call().map { lessonSchedule ->
           lessonSchedule.toLessonScheduleType()
       }
    }

    suspend fun lessonSchedule(id: ID): LessonScheduleType {
        TODO()
    }
}

private fun LessonSchedule.toLessonScheduleType() = LessonScheduleType(

)
