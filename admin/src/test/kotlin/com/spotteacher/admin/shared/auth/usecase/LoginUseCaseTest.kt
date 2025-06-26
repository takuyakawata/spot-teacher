package com.spotteacher.admin.shared.auth.usecase

import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.admin.shared.auth.domain.TokenIssuer
import com.spotteacher.admin.shared.auth.domain.TokenPair
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder

class LoginUseCaseTest : DescribeSpec({
    describe("LoginUseCase") {
        // Arrange - Setup mocks
        val authenticator = mockk<Authenticator>()
        val tokenIssuer = mockk<TokenIssuer>()
        val passwordEncoder = mockk<PasswordEncoder>()
        val useCase = LoginUseCase(authenticator, tokenIssuer)

        describe("call") {
            context("when authentication is successful") {
                it("should return a token pair") {
                    // Arrange
                    val email = EmailAddress("test@example.com")
                    val password = Password("password123")
                    val input = LoginUseCaseInput(email, password)

                    val authUser = AuthUser(email, password)
                    val tokenPair = TokenPair("access-token", "refresh-token")

                    coEvery { authenticator.authenticate(email, password.value) } returns authUser
                    coEvery { tokenIssuer.issueToken(authUser) } returns tokenPair

                    // Act
                    val result = useCase.call(input)

                    // Assert
                    result.isSuccess shouldBe true
                    result.getOrNull() shouldBe tokenPair

                    // Verify
                    coVerify(exactly = 1) { authenticator.authenticate(email, password.value) }
                    coVerify(exactly = 1) { tokenIssuer.issueToken(authUser) }
                }
            }

            context("when authentication fails") {
                it("should return a failure") {
                    // Arrange
                    val email = EmailAddress("test@example.com")
                    val password = Password("wrong-password")
                    val input = LoginUseCaseInput(email, password)

                    val exception = RuntimeException("Authentication failed")

                    coEvery { authenticator.authenticate(email, password.value) } throws exception

                    // Act
                    val result = useCase.call(input)

                    // Assert
                    result.isFailure shouldBe true
                    result.exceptionOrNull()?.message shouldBe "Authentication failed"

                }
            }
        }
    }
})
