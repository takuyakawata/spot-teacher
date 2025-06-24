package com.spotteacher.admin.feature.lessonPlan.handler.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.feature.lessonPlan.handler.DraftLessonPlanType
import com.spotteacher.admin.feature.lessonPlan.handler.LessonPlanDateType
import com.spotteacher.admin.feature.lessonPlan.handler.LessonPlanType
import com.spotteacher.admin.feature.lessonPlan.handler.PublishedLessonPlanType
import com.spotteacher.admin.feature.lessonPlan.handler.toGraphQLID
import com.spotteacher.admin.feature.lessonPlan.usecase.FindLessonPlanUseCase
import com.spotteacher.admin.feature.lessonPlan.usecase.FindLessonPlanUseCaseInput
import com.spotteacher.graphql.toDomainId
import org.springframework.stereotype.Component

sealed interface LessonPlanQueryOutput
data class LessonPlanQuerySuccessOutput(
    val lessonPlan: LessonPlanType
) : LessonPlanQueryOutput

data class LessonPlanQueryErrorOutput(
    val code: LessonPlanErrorCode,
    val message: String
) : LessonPlanQueryOutput

@Component
class LessonPlanQuery(
    private val findLessonPlanUseCase: FindLessonPlanUseCase
) : Query {
    suspend fun lessonPlan(id: ID): LessonPlanQueryOutput {
        val result = findLessonPlanUseCase.call(FindLessonPlanUseCaseInput(id.toDomainId { LessonPlanId(it) }))

        return result.fold(
            ifLeft = {
                LessonPlanQueryErrorOutput(
                    code = it.code,
                    message = it.message
                )
            },
            ifRight = {
                LessonPlanQuerySuccessOutput(
                    lessonPlan = when (it) {
                        is DraftLessonPlan -> DraftLessonPlanType(
                            id = it.id.toGraphQLID(),
                            companyId = it.companyId,
                            title = it.title?.value,
                            description = it.description?.value,
                            lessonType = it.lessonType,
                            location = it.location?.value,
                            annualMaxExecutions = it.annualMaxExecutions,
                            lessonPlanDates = it.lessonPlanDates?.map { date ->
                                LessonPlanDateType(
                                    startMonth = date.startMonth,
                                    startDay = date.startDay,
                                    endMonth = date.endMonth,
                                    endDay = date.endDay,
                                    startTime = date.startTime,
                                    endTime = date.endTime,
                                )
                            },
                            images = it.images,
                            createdAt = it.createdAt,
                        )

                        is PublishedLessonPlan -> PublishedLessonPlanType(
                            id = it.id.toGraphQLID(),
                            companyId = it.companyId,
                            title = it.title.value,
                            description = it.description.value,
                            lessonType = it.lessonType,
                            location = it.location.value,
                            annualMaxExecutions = it.annualMaxExecutions,
                            lessonPlanDates = it.lessonPlanDates.map { date ->
                                LessonPlanDateType(
                                    startMonth = date.startMonth,
                                    startDay = date.startDay,
                                    endMonth = date.endMonth,
                                    endDay = date.endDay,
                                    startTime = date.startTime,
                                    endTime = date.endTime,
                                )
                            },
                            images = it.images,
                            createdAt = it.createdAt,
                        )
                    }
                )
            }
        )
    }
}
