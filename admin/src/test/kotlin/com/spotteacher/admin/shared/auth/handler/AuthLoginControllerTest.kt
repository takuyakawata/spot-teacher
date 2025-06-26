package com.spotteacher.admin.shared.auth.handler

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.EmailAddress
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.datatest.withData
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthLoginControllerTest(
    private val webTestClient: WebTestClient,
    private val adminUserFixture: AdminUserFixture
) : DatabaseDescribeSpec({
    describe("POST /api/admin/auth/login") {
        context("正常な認証情報の場合") {
            lateinit var activeAdminUser: ActiveAdminUser
            var correctPassword: Password

            beforeTest {
                correctPassword = Password("test1234")
                activeAdminUser = adminUserFixture.createActiveAdminUser(
                    email = EmailAddress("test-correct@example.com"),
                    password = correctPassword
                )
            }
            it("200 OKとトークンを返す") {
                // --- 準備 (Arrange) ---
                val password = Password("test1234")
                val loginRequest = LoginRequest(activeAdminUser.email.value, password.value)

                // Act
                webTestClient.post().uri("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange() // リクエスト実行
                    .expectStatus().isOk // ステータスコードが200 OKであること
                    .expectBody(AuthResponse::class.java) // レスポンスボディをAuthResponseクラスとして取得
                    .returnResult() // 結果を取得
                    .responseBody
            }
        }

        context("不正な認証情報の場合") {
            it("401 Unauthorizedを返す") {
                adminUserFixture.createActiveAdminUser(
                    email = EmailAddress("existing@example.com"),
                    password = Password("correctpassword")
                )

                val loginRequest = LoginRequest("existing@example.com", "wrong_password")

                webTestClient.post().uri("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange()
                    .expectStatus().isUnauthorized
            }
        }

        context("存在しないユーザーの場合") {
            it("401 Unauthorizedを返す") {
                val loginRequest = LoginRequest("nonexistent@example.com", "any_password")

                webTestClient.post().uri("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange()
                    .expectStatus().isUnauthorized
                    .expectBody().isEmpty
            }
        }
    }
})
