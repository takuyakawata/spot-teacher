package com.spotteacher.admin.feature.company.domain

interface CompanyRepository {
    suspend fun create(company: Company): Company
    suspend fun update(company: Company)
    suspend fun delete(companyId: CompanyId)
    suspend fun findById(companyId: CompanyId): Company?
    suspend fun getAll(): List<Company>
    suspend fun findByName(name: CompanyName): Company?
}
