package com.spotteacher.teacher.fixture

import com.spotteacher.teacher.feature.company.domain.Company
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.company.domain.CompanyName
import com.spotteacher.domain.Address
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import org.springframework.stereotype.Component
import java.net.URI

@Component
class CompanyFixture {

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
}

fun defaultAddress() = Address(
    postCode = PostCode("1234567"),
    prefecture = Prefecture.TOKYO,
    city = City("Tokyo"),
    streetAddress = StreetAddress("Test Street"),
    buildingName = null,
)