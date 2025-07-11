package com.spotteacher.teacher.feature.company.domain

interface CompanyRepository {
    suspend fun findById(companyId: CompanyId): Company?
}
