package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import org.mockito.Mockito.mock

class CreateAdminUserUseCaseTest : DescribeSpec({
    describe("CreateAdminUserUseCase") {
        // Arrange
        val adminUserRepository = mock<AdminUserRepository>()
        val passwordEncoder = mock<org.springframework.security.crypto.password.PasswordEncoder>()
        val useCase = CreateAdminUserUseCase(
            adminUserRepository = adminUserRepository,
            passwordEncoder = passwordEncoder
        )
        val adminUser = AdminUserFixture().buildActiveAdminUser()

        // Test data
        val firstName = AdminUserName("John")
        val lastName = AdminUserName("Doe")
        val email = EmailAddress("john.doe@example.com")
        val password = Password("password123")
        val confirmPassword = Password("password123")
        val mismatchPassword = Password("different123")

        describe("execute") {
            context("when passwords match") {
                it("should create a new admin user and return success") {
                    // Arrange
                    coEvery { adminUserRepository.create(adminUser, password.value)} returns adminUser

                    // Act
                    val result = useCase.call(
                        CreateAdminUserUseCaseInput(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword
                        )
                    )

                    // Assert
                    result.shouldBeInstanceOf<CreateAdminUserUseCaseSuccess>()
                    val adminUser = result.adminUser
                    adminUser.shouldBeInstanceOf<ActiveAdminUser>()
                    adminUser.firstName shouldBe firstName
                    adminUser.lastName shouldBe lastName
                    adminUser.email shouldBe email
                }
            }

            context("when passwords don't match") {
                it("should return an error") {
                    // Act
                    val result = useCase.call(
                        CreateAdminUserUseCaseInput(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            confirmPassword = mismatchPassword
                        )
                    )

                    // Assert
                    result.shouldBeInstanceOf<CreateAdminUserUseCaseError>()
                    result.message shouldBe "Password and confirmation do not match"
                }
            }
        }
    }
})
