package com.spotteacher.admin.feature.lessonTag.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import com.spotteacher.admin.feature.lessonTag.domain.EducationError
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

@UseCase
class DeleteEducationUseCase(
    private val educationRepository: EducationRepository
) {
    @TransactionCoroutine
    suspend fun call(id: EducationId): Either<EducationError, Unit> {
        // Check if education exists
        educationRepository.filterByIds(nonEmptyListOf(id)).firstOrNull()
            ?: return EducationError(
                code = EducationErrorCode.EDUCATION_NOT_FOUND,
                message = "Education not found"
            ).left()

        //TODO  Check if any lessons are using this education

        educationRepository.delete(id)

        // Return success
        return Unit.right()
    }
}
