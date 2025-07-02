package com.spotteacher.admin.feature.company.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.company.domain.CompanyError
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.usecase.UseCase
import java.net.URI

data class UpdateCompanyUseCaseInput(
    val companyId: CompanyId,
    val name: CompanyName?,
    val address: Address?,
    val phoneNumber: PhoneNumber?,
    val url: URI?
)

data class UpdateCompanyUseCaseOutput(
    val result: Either<CompanyError, Unit>
)

@UseCase
class UpdateCompanyUseCase(
    private val companyRepository: CompanyRepository
) {
    suspend fun call(input: UpdateCompanyUseCaseInput): UpdateCompanyUseCaseOutput {
        val company = companyRepository.findById(input.companyId) ?: return UpdateCompanyUseCaseOutput(
            CompanyError(
                code = CompanyErrorCode.COMPANY_NOT_FOUND,
                message = "Company not found"
            ).left()
        )

        val updatedCompany = company.update(
            name = input.name ?: company.name,
            address = input.address ?: company.address,
            phoneNumber = input.phoneNumber ?: company.phoneNumber,
            url = input.url ?: company.url
        )

        companyRepository.update(updatedCompany)

        return UpdateCompanyUseCaseOutput(Unit.right())
    }
}
