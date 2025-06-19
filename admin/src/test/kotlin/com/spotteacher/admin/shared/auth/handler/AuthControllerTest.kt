package com.spotteacher.admin.shared.auth.handler

import com.spotteacher.backend.DatabaseDescribeSpec
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

// TODO 500 errorだが、テスト実行はできているので、後で調査する

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest(
    private val webTestClient: WebTestClient,
) : DatabaseDescribeSpec({
    describe("POST /api/auth/login") {
        context("正常な認証情報の場合") {
            it("200 OKとトークンを返す") {
                // --- 準備 (Arrange) ---
                val loginRequest = LoginRequest("test@example.com", "password")

                // --- 実行 (Act) & 検証 (Assert) ---
                webTestClient.post().uri("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
            }
        }

        context("不正な認証情報の場合") {
            it("401 Unauthorizedを返す") {
                // --- 準備 (Arrange) ---
                val loginRequest = LoginRequest("wrong@example.com", "wrong_password")
                // --- 実行 (Act) & 検証 (Assert) ---
                webTestClient.post().uri("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(loginRequest)
                    .exchange()
                    .expectStatus().isUnauthorized
            }
        }
    }
})
