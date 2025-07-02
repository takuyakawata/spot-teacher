package com.spotteacher.admin.feature.company.usecase

import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyError
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.domain.Address
import com.spotteacher.domain.BuildingName
import com.spotteacher.domain.City
import com.spotteacher.domain.PhoneNumber
import com.spotteacher.domain.PostCode
import com.spotteacher.domain.Prefecture
import com.spotteacher.domain.StreetAddress
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import java.net.URI
import java.time.LocalDateTime

class CreateCompanyUseCaseTest : DescribeSpec({
    describe("CreateCompanyUseCase") {
        // Arrange
        val companyRepository = mockk<CompanyRepository>()
        val useCase = CreateCompanyUseCase(companyRepository)

        // Test data
        val companyName = CompanyName("Test Company")
        val address = Address(
            postCode = PostCode("1234567890"),
            prefecture = Prefecture.TOKYO,
            city = City("Test City"),
            streetAddress = StreetAddress("Test Street 1-2-3"),
            buildingName = BuildingName("Test Building")
        )
        val phoneNumber = PhoneNumber("1234567890")
        val url = URI("https://example.com")

        val company = Company(
            id = CompanyId(1),
            name = companyName,
            address = address,
            phoneNumber = phoneNumber,
            url = url,
            createdAt = LocalDateTime.now(),
        )
        describe("call") {
            context("when company does not exist") {
                it("should create a new company and return success") {
                    // Arrange
                    coEvery { companyRepository.findByName(companyName) } returns null
                    coEvery { companyRepository.create(any()) } returns company

                    // Act
                    val result = useCase.call(
                        CreateCompanyUseCaseInput(
                            name = companyName,
                            address = address,
                            phoneNumber = phoneNumber,
                            url = url
                        )
                    )

                    // Assert
                    result.result.isRight() shouldBe true
                    result.result shouldBe company.right()
                }
            }

            context("when company already exists") {
                it("should return an error") {
                    // Arrange
                    coEvery { companyRepository.findByName(companyName) } returns company

                    // Act
                    val result = useCase.call(
                        CreateCompanyUseCaseInput(
                            name = companyName,
                            address = address,
                            phoneNumber = phoneNumber,
                            url = url
                        )
                    )

                    // Assert
                    val expectedError = CompanyError(
                        message = "Company already exists",
                        code = CompanyErrorCode.COMPANY_ALREADY_EXISTS
                    )
                    result shouldBe CreateCompanyUseCaseOutput(expectedError.left())
                }
            }
        }
    }
})
