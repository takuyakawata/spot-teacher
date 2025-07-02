package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.auth.domain.RefreshToken
import com.spotteacher.backend.DatabaseDescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class RefreshTokenRepositoryImplTest(
    private val repository: RefreshTokenRepositoryImpl,
    private val adminUserFixture: AdminUserFixture
) : DatabaseDescribeSpec({

    describe("save") {
        it("should save a refresh token and return it with a generated ID") {
            // Arrange
            val adminUser = adminUserFixture.createActiveAdminUser()
            val token = "test-refresh-token"
            val expiresAt = LocalDateTime.now().plusDays(7)

            val refreshToken = RefreshToken.create(
                adminUserId = adminUser.id,
                token = token,
                expiresAt = expiresAt
            )

            // Act
            val savedToken = repository.save(refreshToken)

            // Assert
            savedToken.id.value shouldNotBe 0L // ID should be generated
            savedToken.userId shouldBe adminUser.id
            savedToken.token shouldBe token
            savedToken.expiresAt shouldBe expiresAt
        }
    }
})
