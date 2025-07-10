package com.spotteacher.teacher.feature.company.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.spotteacher.teacher.feature.company.domain.Company
import com.spotteacher.teacher.feature.company.domain.CompanyError
import com.spotteacher.teacher.feature.company.domain.CompanyErrorCode
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.company.domain.CompanyRepository
import com.spotteacher.usecase.UseCase

data class FindCompanyUseCaseOutput(
    val result: Either<CompanyError, Company>
)

@UseCase
class FindCompanyUseCase(
    private val companyRepository: CompanyRepository
) {
    suspend fun call(companyId: CompanyId): FindCompanyUseCaseOutput {
        val company = companyRepository.findById(companyId) ?: return FindCompanyUseCaseOutput(
            CompanyError(
                message = "Company not found",
                code = CompanyErrorCode.COMPANY_NOT_FOUND,
            ).left()
        )

        return FindCompanyUseCaseOutput(company.right())
    }
}
