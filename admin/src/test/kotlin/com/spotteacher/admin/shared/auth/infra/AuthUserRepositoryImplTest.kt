package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.EmailAddress
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class AuthUserRepositoryImplTest(
    private val repository: AuthUserRepositoryImpl,
    private val fixture: AdminUserFixture,
    private val passwordEncoder: PasswordEncoder
) : DatabaseDescribeSpec({
    val email = EmailAddress("test@example.com")
    val password = Password("password123")

    describe("findByEmail") {
        context("when user exists") {
            it("returns the user with correct email and password") {
                // Arrange
                val adminUser = fixture.createActiveAdminUser(
                    email = email,
                    password = password
                )

                // Act
                val authUser = repository.findByEmail(email)

                // Assert
                authUser.email shouldBe email
                authUser.password.value.isNotEmpty() shouldBe true
                passwordEncoder.matches(password.value, authUser.password.value) shouldBe true
            }
        }

        context("when user does not exist") {
            it("throws NullPointerException") {
                // Act & Assert
                shouldThrow<NullPointerException> {
                    repository.findByEmail(EmailAddress("nonexistent@example.com"))
                }
            }
        }
    }
})
