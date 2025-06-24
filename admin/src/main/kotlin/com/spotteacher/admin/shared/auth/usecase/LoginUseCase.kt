package com.spotteacher.admin.shared.auth.usecase

import com.spotteacher.admin.shared.auth.domain.Authenticator
import com.spotteacher.admin.shared.auth.domain.TokenIssuer
import com.spotteacher.admin.shared.auth.domain.TokenPair
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.domain.EmailAddress
import com.spotteacher.usecase.UseCase
import org.springframework.security.crypto.password.PasswordEncoder

data class LoginUseCaseInput(
    val email: EmailAddress,
    val password: Password
)

@UseCase
class LoginUseCase(
    private val authenticator: Authenticator,
    private val tokenIssuer: TokenIssuer,
    private val passwordEncoder: PasswordEncoder,
) {
    suspend fun call(input: LoginUseCaseInput): Result<TokenPair> {
        // 1. 認証処理を依頼
        return  runCatching {
            val authenticatedUser = authenticator.authenticate(input.email, input.password)

            // 2. 認証されたユーザー情報に基づいてトークン発行を依頼
            tokenIssuer.issueToken(authenticatedUser)
        }


    }
}
