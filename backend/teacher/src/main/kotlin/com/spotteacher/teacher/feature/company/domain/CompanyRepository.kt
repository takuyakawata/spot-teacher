package com.spotteacher.teacher.feature.company.domain

import org.jetbrains.annotations.TestOnly

interface CompanyRepository {
    suspend fun findById(companyId: CompanyId): Company?

    @TestOnly
    suspend fun create(company: Company): Company
}
