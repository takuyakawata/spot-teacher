package com.spotteacher.admin.feature.company.infra

import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.Address
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.Companies.Companion.COMPANIES
import com.spotteacher.infra.db.tables.records.CompaniesRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Component
import java.net.URI

@Component
class CompanyRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
): CompanyRepository {
    override suspend fun create(company: Company): Company {
        val id = dslContext.get().insertInto(
            COMPANIES,
            COMPANIES.NAME,
            COMPANIES.POST_CODE,
            COMPANIES.PREFECTURE,
            COMPANIES.CITY,
            COMPANIES.STREET,
            COMPANIES.URL,
            COMPANIES.PHONE_NUMBER,
        ).values(
            company.name.value,
            company.address.postCode.value,
            company.address.prefecture.name,
            company.address.city.value,
            company.address.streetAddress.value,
            company.url?.toString(),
            company.phoneNumber?.value,
        ).returning(COMPANIES.ID).awaitFirstOrNull()?.id!!

        return company.copy(id = CompanyId(id))
    }

    override suspend fun update(company: Company) {
        dslContext.get().update(COMPANIES)
            .set(COMPANIES.NAME, company.name.value)
            .set(COMPANIES.POST_CODE, company.address.postCode.value)
            .set(COMPANIES.PREFECTURE, company.address.prefecture.name)
            .set(COMPANIES.CITY, company.address.city.value)
            .set(COMPANIES.STREET, company.address.streetAddress.value)
            .set(COMPANIES.URL, company.url?.toString())
            .set(COMPANIES.PHONE_NUMBER, company.phoneNumber?.value)
            .where(COMPANIES.ID.eq(company.id.value))
            .awaitLast()
    }

    override suspend fun delete(companyId: CompanyId) {
        dslContext.get().deleteFrom(COMPANIES)
            .where(COMPANIES.ID.eq(companyId.value))
            .awaitLast()
    }

    override suspend fun findById(companyId: CompanyId): Company? {
        return dslContext.get().nonBlockingFetchOne(
            COMPANIES,
            COMPANIES.ID.eq(companyId.value)
        )?.toCompanyEntity()
    }

    override suspend fun getAll(): List<Company> {
        return dslContext.get().nonBlockingFetch(COMPANIES).map { it.toCompanyEntity() }
    }

    override suspend fun findByName(name: CompanyName): Company?{
        return dslContext.get().nonBlockingFetchOne(
            COMPANIES,
            COMPANIES.NAME.eq(name.value)
        )?.toCompanyEntity()
    }


    private fun CompaniesRecord.toCompanyEntity(): Company {
        return Company(
            id = CompanyId(id!!),
            name = CompanyName(name),
            address = Address(
                postCode = PostCode(postCode),
                prefecture = Prefecture.valueOf(prefecture),
                city = City(city),
                streetAddress = StreetAddress(street!!),
                buildingName = null
            ),
            phoneNumber = PhoneNumber(phoneNumber),
            url = url?.let { URI(it) },
            createdAt = createdAt!!
        )
    }
}
