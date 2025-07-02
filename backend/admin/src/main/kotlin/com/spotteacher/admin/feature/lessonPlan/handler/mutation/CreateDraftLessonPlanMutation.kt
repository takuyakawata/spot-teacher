package com.spotteacher.admin.feature.lessonPlan.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.handler.LessonPlanDateInput
import com.spotteacher.admin.feature.lessonPlan.usecase.CreateDraftLessonPlanUseCase
import com.spotteacher.admin.feature.lessonPlan.usecase.CreateDraftLessonPlanUseCaseInput
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.graphql.NonEmptyString
import com.spotteacher.graphql.toDomainId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

data class CreateDraftLessonPlanMutationInput(
    val companyId: ID,
    val title: NonEmptyString?,
    val description: NonEmptyString?,
    val publish: Boolean = false,
    val lessonType: LessonType?,
    val location: NonEmptyString?,
    val annualMaxExecutions: Int?,
    val lessonPlanDates: List<LessonPlanDateInput>?,
    val educations: List<ID>,
    val subjects: List<Subject>,
    val grades: List<Grade>
)

sealed interface CreateDraftLessonPlanMutationOutput
data class CreateDraftLessonPlanMutationSuccess(val result: Unit) : CreateDraftLessonPlanMutationOutput
data class CreateDraftLessonPlanMutationError(
    val message: String,
    val code: LessonPlanErrorCode
) : CreateDraftLessonPlanMutationOutput

@Component
class CreateDraftLessonPlanMutation(
    private val createDraftLessonPlanUseCase: CreateDraftLessonPlanUseCase
) : Mutation {
    @PreAuthorize("isAuthenticated()")
    suspend fun createDraftLessonPlan(
        input: CreateDraftLessonPlanMutationInput
    ): CreateDraftLessonPlanMutationOutput {
        try {
            val useCaseInput = CreateDraftLessonPlanUseCaseInput(
                companyId = CompanyId(input.companyId.value.toLong()),
                title = input.title?.let { LessonPlanTitle(it.value) },
                description = input.description?.let { LessonPlanDescription(it.value) },
                publish = input.publish,
                lessonType = input.lessonType,
                location = input.location?.let { LessonLocation(it.value) },
                annualMaxExecutions = input.annualMaxExecutions,
                lessonPlanDates = input.lessonPlanDates?.map { dateInput ->
                    LessonPlanDate(
                        startMonth = dateInput.startMonth,
                        startDay = dateInput.startDay,
                        endMonth = dateInput.endMonth,
                        endDay = dateInput.endDay,
                        startTime = dateInput.startTime,
                        endTime = dateInput.endTime
                    )
                },
                educations = LessonPlanEducations(input.educations.map { it.toDomainId(::EducationId) }.toSet()),
                subjects = LessonPlanSubjects(input.subjects.toSet()),
                grades = LessonPlanGrades(input.grades.toSet()),
            )

            createDraftLessonPlanUseCase.call(useCaseInput)
            return CreateDraftLessonPlanMutationSuccess(Unit)
        } catch (e: Exception) {
            return CreateDraftLessonPlanMutationError(
                message = e.message ?: "Failed to create draft lesson plan",
                code = LessonPlanErrorCode.LESSON_PLAN_ALREADY_EXISTS
            )
        }
    }
}
