package com.spotteacher.admin.feature.company.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.company.domain.CompanyError
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.usecase.UseCase

data class DeleteCompanyUseCaseInput(
    val id: CompanyId
)

data class DeleteCompanyUseCaseOutput(
    val result: Either<CompanyError, Unit>
)

@UseCase
class DeleteCompanyUseCase(
    private val companyRepository: CompanyRepository
) {
    @TransactionCoroutine
    suspend fun call(input: DeleteCompanyUseCaseInput): DeleteCompanyUseCaseOutput {
        // Check if company exists
        val existingCompany = companyRepository.findById(input.id) ?: return DeleteCompanyUseCaseOutput(
            CompanyError(
                code = CompanyErrorCode.COMPANY_NOT_FOUND,
                message = "Company not found"
            ).left()
        )

        // Delete company from repository
        companyRepository.delete(input.id)

        // Return success
        return DeleteCompanyUseCaseOutput(Unit.right())
    }
}
