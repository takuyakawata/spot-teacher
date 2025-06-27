package com.spotteacher.admin.feature.adminUser.usecase

import arrow.core.Either
import com.spotteacher.admin.feature.adminUser.domain.AdminUserError
import com.spotteacher.admin.feature.adminUser.domain.AdminUserErrorCode
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.domain.Password
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.mockk

class UpdatePasswordUseCaseTest : DescribeSpec({
    describe("UpdatePasswordUseCase") {
        // Arrange
        val adminUserRepository = mockk<AdminUserRepository>()
        val useCase = UpdatePasswordUseCase(adminUserRepository)
        val adminUser = AdminUserFixture().buildActiveAdminUser()

        // Test data
        val newPassword = Password("newpassword123")
        val confirmPassword = Password("newpassword123")
        val mismatchPassword = Password("different123")

        describe("call") {
            context("when passwords match") {
                it("should update password and return success") {
                    // Arrange
                    coEvery { adminUserRepository.findById(adminUser.id) } returns adminUser
                    coEvery { adminUserRepository.updatePassword(any()) } returns Unit

                    // Act
                    val result = useCase.call(
                        UpdatePasswordUseCaseInput(
                            adminUserId = adminUser.id,
                            password = newPassword,
                            confirmPassword = confirmPassword
                        )
                    )

                    // Assert
                    result shouldBe Either.Right(Unit)
                }
            }

            context("when passwords don't match") {
                it("should throw an IllegalArgumentException") {
                    // Act & Assert
                    val exception = io.kotest.assertions.throwables.shouldThrow<IllegalArgumentException> {
                        useCase.call(
                            UpdatePasswordUseCaseInput(
                                adminUserId = adminUser.id,
                                password = newPassword,
                                confirmPassword = mismatchPassword
                            )
                        )
                    }

                    exception.message shouldBe "Password and confirmation do not match"
                }
            }

            context("when admin user is not found") {
                it("should return an error") {
                    // Arrange
                    coEvery { adminUserRepository.findById(adminUser.id) } returns null

                    // Act
                    val result = useCase.call(
                        UpdatePasswordUseCaseInput(
                            adminUserId = adminUser.id,
                            password = newPassword,
                            confirmPassword = newPassword
                        )
                    )

                    // Assert
                    result.shouldBeTypeOf<Either.Left<AdminUserError>>()
                    val error = (result as Either.Left<AdminUserError>).value
                    error.code shouldBe AdminUserErrorCode.ADMIN_USER_NOT_FOUND
                    error.message shouldBe "Admin user not found"
                }
            }
        }
    }
})
