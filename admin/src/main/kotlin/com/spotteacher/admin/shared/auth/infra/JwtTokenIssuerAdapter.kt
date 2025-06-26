package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.config.auth.JwtProvider
import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.auth.domain.RefreshTokenManager
import com.spotteacher.admin.shared.auth.domain.TokenIssuer
import com.spotteacher.admin.shared.auth.domain.TokenPair
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtTokenIssuerAdapter(
    private val jwtProvider: JwtProvider,
    private val refreshTokenManager: RefreshTokenManager
) : TokenIssuer {
    override suspend fun issueToken(user: AuthUser): TokenPair {
        val authenticationForJwt = UsernamePasswordAuthenticationToken(
            user.email.value,
            null,
        )

        val accessToken = jwtProvider.createAccessToken(authenticationForJwt)
        val refreshToken = refreshTokenManager.createAndSaveRefreshToken(user.email)

        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken.token
        )
    }
}
