package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.toNonEmptyListOrNull
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class CreateDraftLessonPlanUseCaseInput(
    val companyId: CompanyId,
    val title: LessonPlanTitle?,
    val description: LessonPlanDescription?,
    val publish: Boolean,
    val lessonType: LessonType?,
    val location: LessonLocation?,
    val annualMaxExecutions: Int?,
    val lessonPlanDates: List<LessonPlanDate>?
)

@UseCase
class CreateDraftLessonPlanUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    @TransactionCoroutine
    suspend fun call(input: CreateDraftLessonPlanUseCaseInput) {
        val draftLessonPlan = DraftLessonPlan.create(
            companyId = input.companyId,
            title = input.title,
            description = input.description,
            lessonType = input.lessonType,
            location = input.location,
            annualMaxExecutions = input.annualMaxExecutions,
            lessonPlanDates = input.lessonPlanDates?.toNonEmptyListOrNull()
        )

        lessonPlanRepository.createDraft(draftLessonPlan)
    }
}
