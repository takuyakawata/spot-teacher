package com.spotteacher.admin.shared.auth.handler

import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.EmailAddress
import io.kotest.assertions.eq.actualIsNull
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

//TODO 500 errorだが、テスト実行はできているので、後で調査する

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthLoginControllerTest(
    private val webTestClient: WebTestClient,
    private val adminUserFixture: AdminUserFixture
) : DatabaseDescribeSpec({
    describe("POST /api/auth/login") {
        context("正常な認証情報の場合") {
            it("200 OKとトークンを返す") {
               val activeAdminUser = adminUserFixture.createActiveAdminUser(email = EmailAddress("test@example.com"))

                // --- 準備 (Arrange) ---
                val loginRequest = LoginRequest(activeAdminUser.email.value, activeAdminUser.password.value)

                // --- 実行 (Act) & 検証 (Assert) ---
                webTestClient.post().uri("/api/auth/login")
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
                // 事前にユーザーを作成する必要はない（存在しないユーザーとしてテスト）
                // ただし、もしユーザーが存在しないとAdminUserRepositoryがエラーを返すので、
                // 存在するがパスワードが間違っているケースをテストする方が実用的。
                // ユーザーはDBにいるが、パスワードが不正な場合を再現
                adminUserFixture.createActiveAdminUser(
                    email = EmailAddress("existing@example.com"),
                    password = Password("correctpassword")
                )

                // --- 準備 (Arrange) ---
                val loginRequest = LoginRequest("existing@example.com", "wrong_password")

                // --- 実行 (Act) & 検証 (Assert) ---
                webTestClient.post().uri("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange()
                    .expectStatus().isUnauthorized // ステータスコードが401 Unauthorizedであること
                    .expectBody().isEmpty // レスポンスボディが空であることを確認（セキュリティのため）
            }
        }

        context("存在しないユーザーの場合") {
            it("401 Unauthorizedを返す") {
                // --- 準備 (Arrange) ---
                val loginRequest = LoginRequest("nonexistent@example.com", "any_password")

                // --- 実行 (Act) & 検証 (Assert) ---
                webTestClient.post().uri("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange()
                    .expectStatus().isUnauthorized // ステータスコードが401 Unauthorizedであること
                    .expectBody().isEmpty
            }
        }
    }
})
