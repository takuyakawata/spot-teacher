package com.spotteacher.admin.feature.company.usecase

import arrow.core.left
import arrow.core.right
import com.spotteacher.admin.feature.company.domain.Company
import com.spotteacher.admin.feature.company.domain.CompanyError
import com.spotteacher.admin.feature.company.domain.CompanyErrorCode
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyName
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.admin.fixture.CompanyFixture
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
import io.mockk.coVerify
import io.mockk.mockk
import java.net.URI
import java.time.LocalDateTime

class UpdateCompanyUseCaseTest : DescribeSpec({
    describe("UpdateCompanyUseCase") {
        // Arrange
        val companyRepository = mockk<CompanyRepository>()
        val useCase = UpdateCompanyUseCase(companyRepository)
        val fixture = CompanyFixture()

        val company = fixture.buildCompany()

        val companyId = CompanyId(1)
        val originalName = CompanyName("Original Company")
        val originalAddress = Address(
            postCode = PostCode("1234567890"),
            prefecture = Prefecture.TOKYO,
            city = City("Original City"),
            streetAddress = StreetAddress("Original Street 1-2-3"),
            buildingName = BuildingName("Original Building")
        )
        val originalPhoneNumber = PhoneNumber("1234567890")
        val originalUrl = URI("https://original-example.com")

        val originalCompany = Company(
            id = companyId,
            name = originalName,
            address = originalAddress,
            phoneNumber = originalPhoneNumber,
            url = originalUrl,
            createdAt = LocalDateTime.now()
        )

        val updatedName = CompanyName("Updated Company")
        val updatedAddress = Address(
            postCode = PostCode("0987654321"),
            prefecture = Prefecture.OSAKA,
            city = City("Updated City"),
            streetAddress = StreetAddress("Updated Street 3-2-1"),
            buildingName = BuildingName("Updated Building")
        )
        val updatedPhoneNumber = PhoneNumber("0987654321")
        val updatedUrl = URI("https://updated-example.com")

        val updatedCompany = Company(
            id = companyId,
            name = updatedName,
            address = updatedAddress,
            phoneNumber = updatedPhoneNumber,
            url = updatedUrl,
            createdAt = originalCompany.createdAt
        )

        describe("call") {
            context("when company exists") {
                it("should update the company and return success") {
                    // Arrange
                    coEvery { companyRepository.findById(company.id) } returns originalCompany
                    coEvery { companyRepository.update(any()) } returns Unit

                    // Act
                    val result = useCase.call(
                        UpdateCompanyUseCaseInput(
                            companyId = company.id,
                            name = updatedName,
                            address = updatedAddress,
                            phoneNumber = updatedPhoneNumber,
                            url = updatedUrl
                        )
                    )

                    // Assert
                    result.result shouldBe Unit.right()
                }

                it("should update only specified fields") {
                    // Arrange
                    coEvery { companyRepository.findById(companyId) } returns originalCompany
                    coEvery { companyRepository.update(any()) } returns Unit

                    // Only update name and phone number
                    val partiallyUpdatedCompany = Company(
                        id = companyId,
                        name = updatedName,
                        address = originalAddress,
                        phoneNumber = updatedPhoneNumber,
                        url = originalUrl,
                        createdAt = originalCompany.createdAt
                    )

                    // Act
                    val result = useCase.call(
                        UpdateCompanyUseCaseInput(
                            companyId = companyId,
                            name = updatedName,
                            address = null,
                            phoneNumber = updatedPhoneNumber,
                            url = null
                        )
                    )

                    // Assert
                    result.result shouldBe Unit.right()
                    coVerify { companyRepository.update(partiallyUpdatedCompany) }
                }
            }

            context("when company does not exist") {
                it("should return company not found error") {
                    // Arrange
                    coEvery { companyRepository.findById(company.id) } returns null

                    // Act
                    val result = useCase.call(
                        UpdateCompanyUseCaseInput(
                            companyId = company.id,
                            name = updatedName,
                            address = updatedAddress,
                            phoneNumber = updatedPhoneNumber,
                            url = updatedUrl
                        )
                    )

                    // Assert
                    val expectedError = CompanyError(
                        code = CompanyErrorCode.COMPANY_NOT_FOUND,
                        message = "Company not found"
                    )
                    result.result shouldBe expectedError.left()
                }
            }
        }
    }
})
