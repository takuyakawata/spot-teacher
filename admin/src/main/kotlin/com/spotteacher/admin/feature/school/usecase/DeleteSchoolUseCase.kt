package com.spotteacher.admin.feature.school.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.school.domain.SchoolError
import com.spotteacher.admin.feature.school.domain.SchoolErrorCode
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.domain.SchoolRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class DeleteSchoolUseCaseInput(
    val schoolId: SchoolId
)

data class DeleteSchoolUseCaseOutput(
    val result: Either<SchoolError, Unit>
)

@UseCase
class DeleteSchoolUseCase(
    private val schoolRepository: SchoolRepository
) {
    @TransactionCoroutine
    suspend fun call(input: DeleteSchoolUseCaseInput): DeleteSchoolUseCaseOutput {
        // Check if school exists
        schoolRepository.findById(input.schoolId) ?: return DeleteSchoolUseCaseOutput(
            SchoolError(
                code = SchoolErrorCode.SCHOOL_NOT_FOUND,
                message = "School not found"
            ).left()
        )

        // Delete school from repository
        schoolRepository.delete(input.schoolId)

        // Return success
        return DeleteSchoolUseCaseOutput(Unit.right())
    }
}
