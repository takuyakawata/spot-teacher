package com.spotteacher.admin.feature.lessonPlan.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanError
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanErrorCode
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.update
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class UpdateLessonPlanUseCaseInput(
    val id: LessonPlanId,
    val title: LessonPlanTitle?,
    val description: LessonPlanDescription?,
    val lessonType: LessonType?,
    val location: LessonLocation?,
    val annualMaxExecutions: Int?,
    val images: List<UploadFileId>?,
    val educations: LessonPlanEducations,
    val subjects: LessonPlanSubjects,
    val grades: LessonPlanGrades,
)

@UseCase
class UpdateLessonPlanUseCase(
    private val lessonPlanRepository: LessonPlanRepository
) {
    @TransactionCoroutine
    suspend fun call(input: UpdateLessonPlanUseCaseInput): Either<LessonPlanError, Unit> {
        val lessonPlan = lessonPlanRepository.findById(input.id) ?: return LessonPlanError(
            code = LessonPlanErrorCode.LESSON_PLAN_NOT_FOUND,
            message = "Lesson plan with ID ${input.id.value} not found"
        ).left()

        val description = input.description
        val location = input.location

        val updatedLessonPlan = when (lessonPlan) {
            is PublishedLessonPlan -> {
                // Using the extension function for PublishedLessonPlan
                lessonPlan.update(
                    title = input.title,
                    description = description,
                    lessonType = input.lessonType,
                    location = location,
                    annualMaxExecutions = input.annualMaxExecutions,
                    images = input.images,
                    educations = input.educations,
                    subjects = input.subjects,
                    grades = input.grades,

                )
            }
            is DraftLessonPlan -> {
                // Using the member function for DraftLessonPlan
                lessonPlan.update(
                    title = input.title,
                    description = description,
                    lessonType = input.lessonType,
                    location = location,
                    annualMaxExecutions = input.annualMaxExecutions,
                    images = input.images,
                    educations = input.educations,
                    subjects = input.subjects,
                    grades = input.grades,
                )
            }
        }

        // Save the updated lesson plan
        lessonPlanRepository.update(updatedLessonPlan)

        return Unit.right()
    }
}
