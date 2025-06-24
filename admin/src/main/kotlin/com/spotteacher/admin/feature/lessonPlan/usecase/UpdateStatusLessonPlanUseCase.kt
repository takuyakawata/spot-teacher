package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.toDraftLessonPlan
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

@UseCase
class UpdateStatusLessonPlanUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    @TransactionCoroutine
    suspend fun call(id: LessonPlanId): Either<LessonPlanError, Unit> {
        val lessonPlan = lessonPlanRepository.findById(id) ?: return LessonPlanError(
            code = LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND,
            message = "Lesson plan with ID ${id.value} not found"
        ).left()

        val updatedLessonPlan = when (lessonPlan) {
            is PublishedLessonPlan -> lessonPlan.toDraftLessonPlan()
            is DraftLessonPlan -> lessonPlan.toPublishedLessonPlan()
        }

        lessonPlanRepository.updateStatus(updatedLessonPlan)

        return Unit.right()
    }
}
