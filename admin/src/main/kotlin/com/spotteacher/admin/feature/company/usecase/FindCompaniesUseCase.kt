package com.spotteacher.admin.feature.company.usecase

import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.usecase.UseCase

@UseCase
class FindCompaniesUseCase(
    private val companyRepository: CompanyRepository
) {
    suspend fun call(): List<Company> = companyRepository.getAll()
}
