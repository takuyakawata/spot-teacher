package com.spotteacher.admin.shared.auth.usecase

import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.admin.shared.auth.domain.TokenIssuer
import com.spotteacher.admin.shared.auth.domain.TokenPair
import com.spotteacher.domain.EmailAddress
import com.spotteacher.usecase.UseCase

// data class LoginUseCaseOutput(
//    val result: Either<LoginError, LoginUseCaseOutputSuccess>
// )

// data class LoginUseCaseOutputSuccess(
//    val accessToken: String,
//    val refreshToken: String
// )

// data class LoginError(
//    val message: String,
//    val code: LoginErrorCode
// )

// enum class LoginErrorCode {
//    EMAIL_NOT_FOUND,
//    INVALID_PASSWORD
// }

data class LoginUseCaseInput(
    val email: EmailAddress,
    val password: Password
)

@UseCase
class LoginUseCase(
    private val authenticator: Authenticator,
    private val tokenIssuer: TokenIssuer
) {
    suspend fun call(input: LoginUseCaseInput): TokenPair {
        // 1. 認証処理を依頼
        val authenticatedUser = authenticator.authenticate(input.email, input.password)

        // 2. 認証されたユーザー情報に基づいてトークン発行を依頼
        return tokenIssuer.issueToken(authenticatedUser)
    }
}
