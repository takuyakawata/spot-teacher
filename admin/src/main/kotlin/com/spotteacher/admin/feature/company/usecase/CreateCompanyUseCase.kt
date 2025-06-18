package com.spotteacher.admin.feature.company.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyError
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.admin.shared.infra.TransactionCoroutine
import com.spotteacher.domain.Address
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.usecase.UseCase
import java.net.URI

data class CreateCompanyUseCaseInput(
    val name: CompanyName,
    val address: Address,
    val phoneNumber: PhoneNumber,
    val url: URI?
)

data class CreateCompanyUseCaseOutput(
    val result: Either<CompanyError, Company>
)

@UseCase
class CreateCompanyUseCase(
    private val companyRepository: CompanyRepository
) {
    @TransactionCoroutine
    suspend fun call(input: CreateCompanyUseCaseInput): CreateCompanyUseCaseOutput {
        // Check if company already exists
        require(companyRepository.findByName(input.name) == null) {
            return CreateCompanyUseCaseOutput(
                CompanyError(
                    message =  "Company already exists",
                    code = CompanyErrorCode.COMPANY_ALREADY_EXISTS,
                ).left()
            )
        }

        val newCompany = Company.create(
            name = input.name,
            address = input.address,
            phoneNumber = input.phoneNumber,
            url = input.url
        )

        return CreateCompanyUseCaseOutput(companyRepository.create(newCompany).right())
    }
}
