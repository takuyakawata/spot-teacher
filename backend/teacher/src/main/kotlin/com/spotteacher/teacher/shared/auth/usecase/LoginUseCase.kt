package com.spotteacher.teacher.shared.auth.usecase


import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.shared.auth.domain.Authenticator
import com.spotteacher.teacher.shared.auth.domain.TokenIssuer
import com.spotteacher.teacher.shared.auth.domain.TokenPair
import com.spotteacher.teacher.shared.domain.Password
import com.spotteacher.usecase.UseCase

data class LoginUseCaseInput(
    val email: EmailAddress,
    val password: Password
)

@UseCase
class LoginUseCase(
    private val authenticator: Authenticator,
    private val tokenIssuer: TokenIssuer,
) {
    suspend fun call(input: LoginUseCaseInput): Result<TokenPair> {
        return  runCatching {
            // 1. 認証処理を依頼
            // パスワードはそのままAuthenticatorに渡す（エンコードはAuthenticatorの責任）
            val authenticatedUser = authenticator.authenticate(input.email, input.password.value)

            // 2. 認証されたユーザー情報に基づいてトークン発行を依頼
            tokenIssuer.issueToken(authenticatedUser)
        }
    }
}
