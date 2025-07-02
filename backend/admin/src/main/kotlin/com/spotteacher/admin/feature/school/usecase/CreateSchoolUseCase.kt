package com.spotteacher.admin.feature.school.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.school.domain.School
import com.spotteacher.admin.feature.school.domain.SchoolCategory
import com.spotteacher.admin.feature.school.domain.SchoolError
import com.spotteacher.admin.feature.school.domain.SchoolErrorCode
import com.spotteacher.admin.feature.school.domain.SchoolName
import com.spotteacher.admin.feature.school.domain.SchoolRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.usecase.UseCase

data class CreateSchoolUseCaseInput(
    val name: SchoolName,
    val schoolCategory: SchoolCategory,
    val address: Address,
    val phoneNumber: PhoneNumber,
    val url: String?
)

data class CreateSchoolUseCaseOutput(
    val result: Either<SchoolError, School>
)

@UseCase
class CreateSchoolUseCase(
    private val schoolRepository: SchoolRepository
) {
    @TransactionCoroutine
    suspend fun call(input: CreateSchoolUseCaseInput): CreateSchoolUseCaseOutput {
        // Check if school already exists
        require(schoolRepository.findByName(input.name) == null) {
            return CreateSchoolUseCaseOutput(
                SchoolError(
                    message = "School already exists",
                    code = SchoolErrorCode.SCHOOL_ALREADY_EXISTS,
                ).left()
            )
        }

        val newSchool = School.create(
            name = input.name,
            schoolCategory = input.schoolCategory,
            address = input.address,
            phoneNumber = input.phoneNumber,
            url = input.url
        )

        return CreateSchoolUseCaseOutput(schoolRepository.create(newSchool).right())
    }
}
