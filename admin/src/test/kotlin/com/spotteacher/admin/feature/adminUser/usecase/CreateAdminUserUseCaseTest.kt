package com.spotteacher.admin.feature.adminUser.usecase

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserCreator
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class CreateAdminUserUseCaseTest : DescribeSpec({
    describe("CreateAdminUserUseCase") {
        // Arrange
        val adminUserCreator = mockk<AdminUserCreator>()
        val useCase = CreateAdminUserUseCase(adminUserCreator)

        // Test data
        val firstName = AdminUserName("John")
        val lastName = AdminUserName("Doe")
        val email = EmailAddress("john.doe@example.com")
        val password = Password("password123")
        val confirmPassword = Password("password123")
        val mismatchPassword = Password("different123")

        describe("call") {
            context("when passwords match") {
                it("should create a new admin user and return success") {
                    runTest {
                        // Arrange
                        val expectedAdminUser = ActiveAdminUser.create(
                            firstName = firstName,
                            lastName = lastName,
                            email = email
                        )

                        coEvery { adminUserCreator.createAdminUser(any(), password) } returns Unit

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
                        result.isSuccess shouldBe true
                    }
                }
            }

            context("when passwords don't match") {
                it("should throw IllegalArgumentException") {
                    runTest {
                        // Act & Assert
                        val exception = shouldThrow<IllegalArgumentException> {
                            useCase.call(
                                CreateAdminUserUseCaseInput(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    password = password,
                                    confirmPassword = mismatchPassword
                                )
                            )
                        }

                        exception.message shouldContain "Password and confirmation do not match"
                    }
                }
            }
        }
    }
})
