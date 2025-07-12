package com.spotteacher.teacher.feature.lessonPlan.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.teacher.feature.lessonPlan.handler.LessonPlanType
import com.spotteacher.teacher.feature.lessonPlan.usecase.FindLessonPlanUseCase
import com.spotteacher.teacher.feature.lessonPlan.usecase.FindPaginatedLessonPlansUseCase
import org.springframework.stereotype.Component

@Component
class LessonPlanQuery(
    private val paginatedLessonPlansUseCase: FindPaginatedLessonPlansUseCase,
    private val findLessonPlanUseCase: FindLessonPlanUseCase,
): Query {
    suspend fun lessonPlans():List<LessonPlanType>{
        return emptyList()
    }

    suspend fun lessonPlanById(): LessonPlanType{
        return LessonPlanType(
            id = TODO(),
            companyId = TODO(),
            images = TODO(),
            createdAt = TODO(),
            title = TODO(),
            description = TODO(),
            lessonType = TODO(),
            location = TODO(),
            annualMaxExecutions = TODO(),
            lessonPlanDates = TODO(),
            lessonPlanEducations = TODO(),
            lessonPlanSubjects = TODO(),
            lessonPlanGrades = TODO(),
        )
    }
}
