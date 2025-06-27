package com.spotteacher.admin.feature.lessonTag.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationError
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

@UseCase
class CreateEducationUseCase(
    private val educationRepository: EducationRepository
) {
    @TransactionCoroutine
    suspend fun call(name: EducationName): Either<EducationError, Unit> {
        val existingEducation = educationRepository.findByName(name)
        if (existingEducation != null) {
            return EducationError(
                code = EducationErrorCode.EDUCATION_ALREADY_EXISTS,
                message = "Education already exists"
            ).left()
        }

        val newEducation = Education.create(name = name)
        educationRepository.create(newEducation)

        return Unit.right()
    }
}
