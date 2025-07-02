package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.domain.Address
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URI

@Component
class CompanyFixture {

    @Autowired
    private lateinit var companyRepository: CompanyRepository

    private var companyIdCount = 1L

    fun buildCompany(
        id: CompanyId = CompanyId(companyIdCount++),
        name: CompanyName = CompanyName("test company"),
        address: Address = defaultAddress(),
        phoneNumber: PhoneNumber = PhoneNumber("1234567890"),
        url: URI = URI("https://example.com"),
        createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
    ): Company {
        return Company(
            id = id,
            name = name,
            address = address,
            phoneNumber = phoneNumber,
            url = url,
            createdAt = createdAt,
        )
    }

    suspend fun createCompany(
        name: CompanyName = CompanyName("test company"),
        address: Address = defaultAddress(),
        phoneNumber: PhoneNumber = PhoneNumber("12345678"),
        url: URI = URI("https://example.com"),
        createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
    ): Company {
        val company = buildCompany(
            name = name,
            address = address,
            phoneNumber = phoneNumber,
            url = url,
            createdAt = createdAt,
        )
        return companyRepository.create(company)
    }
}

fun defaultAddress() = Address(
    postCode = PostCode("1234567"),
    prefecture = Prefecture.TOKYO,
    city = City("Tokyo"),
    streetAddress = StreetAddress("Test Street"),
    buildingName = null,
)
