package com.spotteacher.teacher.feature.company.infra

import com.spotteacher.teacher.feature.company.domain.Company
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.company.domain.CompanyName
import com.spotteacher.teacher.feature.company.domain.CompanyRepository
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.Address
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.Companies.Companion.COMPANIES
import com.spotteacher.infra.db.tables.records.CompaniesRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Repository
import java.net.URI

@Repository
class CompanyRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : CompanyRepository {
    override suspend fun findById(companyId: CompanyId): Company? {
        return dslContext.get().nonBlockingFetchOne(
            COMPANIES,
            COMPANIES.ID.eq(companyId.value)
        )?.toCompanyEntity()
    }

    override suspend fun create(company: Company):Company {
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
