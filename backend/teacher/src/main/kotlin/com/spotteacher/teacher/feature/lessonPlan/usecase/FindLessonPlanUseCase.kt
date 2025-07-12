package com.spotteacher.teacher.feature.lessonPlan.usecase

import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindLessonPlanUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    suspend fun call(id: LessonPlanId): Result<LessonPlan> {
        val lessonPlan = lessonPlanRepository.findById(id)
        return if (lessonPlan != null) {
            Result.success(lessonPlan)
        } else {
            Result.failure(
                RuntimeException(
                    LessonPlanError(
                        code = LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND,
                        message = "Lesson plan with id ${id.value} not found"
                    ).toString()
                )
            )
        }
    }
}
