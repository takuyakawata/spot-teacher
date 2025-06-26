package com.spotteacher.admin.feature.lessonTag.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import com.spotteacher.admin.feature.lessonTag.domain.EducationError
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class UpdateEducationUseCaseInput(
    val id: EducationId,
    val name: EducationName?,
    val isActive: Boolean?,
    val displayOrder: Int?
)

@UseCase
class UpdateEducationUseCase(
    private val educationRepository: EducationRepository
) {
    @TransactionCoroutine
    suspend fun call(input: UpdateEducationUseCaseInput):Either<EducationError,Unit> {
        val education = educationRepository.filterByIds(nonEmptyListOf(input.id)).firstOrNull()
            ?: return EducationError(
                code = EducationErrorCode.EDUCATION_NOT_FOUND,
                message = "Education not found"
            ).left()

        val updatedEducation = education.update(
            name = input.name,
            isActive = input.isActive,
            displayOrder = input.displayOrder
        )

        educationRepository.update(updatedEducation)

        return Unit.right()
    }
}
