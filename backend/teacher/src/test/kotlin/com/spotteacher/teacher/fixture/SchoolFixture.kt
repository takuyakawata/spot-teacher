package com.spotteacher.teacher.fixture

import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import com.spotteacher.teacher.feature.school.domain.School
import com.spotteacher.teacher.feature.school.domain.SchoolCategory
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.school.domain.SchoolName
import com.spotteacher.teacher.feature.school.domain.SchoolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SchoolFixture {

    @Autowired
    private lateinit var repository: SchoolRepository

    private var schoolIdCount = 1L

    fun buildSchool(
        id: SchoolId = SchoolId(schoolIdCount++),
        name: String = "Test School",
        schoolCategory: SchoolCategory = SchoolCategory.ELEMENTARY,
        postCode: String = "123-4567",
        prefecture: Prefecture = Prefecture.TOKYO,
        city: String = "Test City",
        streetAddress: String = "Test Street 1-2-3",
        buildingName: String? = "Test Building",
        phoneNumber: String = "03-1234-5678",
        url: String? = "https://example.com"
    ): School {
        return School(
            id = id,
            name = SchoolName(name),
            schoolCategory = schoolCategory,
            address = Address(
                postCode = PostCode(postCode),
                prefecture = prefecture,
                city = City(city),
                streetAddress = StreetAddress(streetAddress),
                buildingName = buildingName?.let { BuildingName(it) }
            ),
            phoneNumber = PhoneNumber(phoneNumber),
            url = url
        )
    }

    suspend fun createSchool(): School {
        val school = buildSchool(id = SchoolId(0))
        return repository.create(school)
    }
}
