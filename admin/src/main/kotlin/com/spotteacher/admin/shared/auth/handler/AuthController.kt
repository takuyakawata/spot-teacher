package com.spotteacher.admin.shared.auth.handler

import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.shared.auth.usecase.LoginUseCase
import com.spotteacher.admin.shared.auth.usecase.LoginUseCaseInput
import com.spotteacher.domain.EmailAddress
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val accessToken: String, val refreshToken: String)

@RestController
@RequestMapping("/api/admin/auth")
class AuthController(private val loginUse: LoginUseCase) {
    @PostMapping("/login")
    suspend fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        val tokenPair = loginUse.call(
            LoginUseCaseInput(
                EmailAddress(loginRequest.email),
                Password(loginRequest.password)
            )
        )

        return tokenPair.let {
            ResponseEntity.ok(
                AuthResponse(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            )
        }
    }
}
