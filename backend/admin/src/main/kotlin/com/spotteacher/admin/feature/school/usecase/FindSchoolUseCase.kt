package com.spotteacher.admin.feature.school.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.school.domain.School
import com.spotteacher.admin.feature.school.domain.SchoolError
import com.spotteacher.admin.feature.school.domain.SchoolErrorCode
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.domain.SchoolRepository
import com.spotteacher.usecase.UseCase

data class FindSchoolUseCaseInput(
    val schoolId: SchoolId
)

data class FindSchoolUseCaseOutput(
    val result: Either<SchoolError, School>
)

@UseCase
class FindSchoolUseCase(
    private val schoolRepository: SchoolRepository
) {
    suspend fun call(input: FindSchoolUseCaseInput): FindSchoolUseCaseOutput {
        val school = schoolRepository.findById(input.schoolId) ?: return FindSchoolUseCaseOutput(
            SchoolError(
                message = "School not found",
                code = SchoolErrorCode.SCHOOL_NOT_FOUND,
            ).left()
        )

        return FindSchoolUseCaseOutput(school.right())
    }
}
