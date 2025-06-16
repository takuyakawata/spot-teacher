package com.spotteacher.admin.feature.company.usecase

import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.company.domain.CompanyRepository
import com.spotteacher.admin.fixture.CompanyFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class FindCompanyUseCaseTest : DescribeSpec({
    describe("FindCompanyUseCase") {
        // Arrange
        val companyRepository = mockk<CompanyRepository>()
        val useCase = FindCompanyUseCase(companyRepository)
        val companyFixture = CompanyFixture()

        describe("call") {
            context("when company exists") {
                it("should return the company") {
                    // Arrange
                    val company = companyFixture.buildCompany()
                    val companyId = company.id

                    coEvery { companyRepository.findById(companyId) } returns company

                    // Act
                    val result = useCase.call(companyId)

                    // Assert
                    result.result.isRight() shouldBe true

                    // Verify
                    coVerify(exactly = 1) { companyRepository.findById(companyId) }
                }
            }

            context("when company does not exist") {
                it("should return an error") {
                    // Arrange
                    val companyId = CompanyId(2) // Use a different ID to avoid conflicts
                    coEvery { companyRepository.findById(companyId) } returns null

                    // Act
                    val result = useCase.call(companyId)

                    // Assert
                    result.result.isLeft() shouldBe true

                    // Verify
                    coVerify(exactly = 1) { companyRepository.findById(companyId) }
                }
            }
        }
    }
})
