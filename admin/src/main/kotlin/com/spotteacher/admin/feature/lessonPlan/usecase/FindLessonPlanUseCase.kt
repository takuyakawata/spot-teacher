package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.Either
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.usecase.UseCase

data class FindLessonPlanUseCaseInput(
    val id: LessonPlanId
)

@UseCase
class FindLessonPlanUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    suspend fun call(input: FindLessonPlanUseCaseInput): Either<LessonPlanError, LessonPlan> {
        val lessonPlan =  lessonPlanRepository.findById(input.id)

        return lessonPlan?.let { Either.Right(it) }
            ?: Either.Left(
                LessonPlanError( // nullの場合はLeftを返す
                    code = LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND,
                    message = "Lesson plan not found"
                )
            )
    }
}
