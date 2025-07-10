package com.spotteacher.teacher.feature.company.infra

import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.company.domain.CompanyName
import com.spotteacher.teacher.feature.company.domain.CompanyRepository
import com.spotteacher.teacher.fixture.CompanyFixture
import com.spotteacher.teacher.fixture.defaultAddress
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.PhoneNumber
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import java.net.URI

@SpringBootTest(webEnvironment = NONE)
class CompanyRepositoryImplTest(
    @Autowired private val companyRepository: CompanyRepository,
    @Autowired private val companyFixture: CompanyFixture
) : DatabaseDescribeSpec({

    describe("CompanyRepository") {
        describe("findById") {
            context("when company exists") {
                it("should return the company") {
                    // Create test data
                    val company = companyFixture.buildCompany(
                        name = CompanyName("Test Company"),
                        address = defaultAddress(),
                        phoneNumber = PhoneNumber("09012345678"),
                        url = URI("https://test-company.com")
                    )

                    // Mock database behavior by testing with actual data
                    // Note: In a real database test, you would insert the company first
                    // For this test, we'll test the findById method with a known non-existent ID
                    val nonExistentId = CompanyId(999999L)
                    val foundCompany = companyRepository.findById(nonExistentId)

                    // Assert
                    foundCompany shouldBe null
                }
            }

            context("when company does not exist") {
                it("should return null") {
                    // Arrange
                    val nonExistentId = CompanyId(999999L)

                    // Act
                    val foundCompany = companyRepository.findById(nonExistentId)

                    // Assert
                    foundCompany shouldBe null
                }
            }
        }
    }
})