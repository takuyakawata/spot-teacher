package com.spotteacher.admin.feature.school.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.school.domain.SchoolCategory
import com.spotteacher.admin.feature.school.domain.SchoolError
import com.spotteacher.admin.feature.school.domain.SchoolErrorCode
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.school.domain.SchoolName
import com.spotteacher.admin.feature.school.domain.SchoolRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.usecase.UseCase

data class UpdateSchoolUseCaseInput(
    val schoolId: SchoolId,
    val name: SchoolName?,
    val schoolCategory: SchoolCategory?,
    val address: Address?,
    val phoneNumber: PhoneNumber?,
    val url: String?
)

data class UpdateSchoolUseCaseOutput(
    val result: Either<SchoolError, Unit>
)

@UseCase
class UpdateSchoolUseCase(
    private val schoolRepository: SchoolRepository
) {
    @TransactionCoroutine
    suspend fun call(input: UpdateSchoolUseCaseInput): UpdateSchoolUseCaseOutput {
        val school = schoolRepository.findById(input.schoolId) ?: return UpdateSchoolUseCaseOutput(
            SchoolError(
                code = SchoolErrorCode.SCHOOL_NOT_FOUND,
                message = "School not found"
            ).left()
        )

        val updatedSchool = school.copy(
            name = input.name ?: school.name,
            schoolCategory = input.schoolCategory ?: school.schoolCategory,
            address = input.address ?: school.address,
            phoneNumber = input.phoneNumber ?: school.phoneNumber,
            url = input.url ?: school.url
        )

        schoolRepository.update(updatedSchool)

        return UpdateSchoolUseCaseOutput(Unit.right())
    }
}
