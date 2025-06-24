package com.spotteacher.admin.shared.auth.handler


import com.spotteacher.admin.shared.auth.usecase.LoginUseCase
import com.spotteacher.admin.shared.auth.usecase.LoginUseCaseInput
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import com.spotteacher.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
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
        val result = loginUse.call(
            LoginUseCaseInput(
                EmailAddress(loginRequest.email),
                Password(loginRequest.password)
            )
        )

        return result.fold(
            onSuccess = { tokenPair ->
                ResponseEntity.ok(
                    AuthResponse(
                        accessToken = tokenPair.accessToken,
                        refreshToken = tokenPair.refreshToken
                    )
                )
            },
            onFailure = { throwable ->
                // ユースケースからスローされる例外をHTTPステータスコードにマッピング
                when (throwable) {
//                    is AuthenticationFailedException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                    is ResourceNotFoundException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                    // その他の予期せぬ例外はINTERNAL_SERVER_ERROR
                    else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
            }
        )
    }
}
