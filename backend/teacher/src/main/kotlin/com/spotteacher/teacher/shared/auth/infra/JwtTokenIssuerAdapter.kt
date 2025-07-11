package com.spotteacher.teacher.shared.auth.infra


import com.spotteacher.teacher.config.auth.JwtProvider
import com.spotteacher.teacher.shared.auth.domain.AuthUser
import com.spotteacher.teacher.shared.auth.domain.RefreshTokenManager
import com.spotteacher.teacher.shared.auth.domain.TokenIssuer
import com.spotteacher.teacher.shared.auth.domain.TokenPair
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
