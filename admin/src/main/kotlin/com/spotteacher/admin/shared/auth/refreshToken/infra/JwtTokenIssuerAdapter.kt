package com.spotteacher.admin.shared.auth.refreshToken.infra

import com.spotteacher.admin.config.auth.JwtProvider
import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.shared.auth.refreshToken.domain.RefreshTokenManager
import com.spotteacher.admin.shared.auth.refreshToken.domain.TokenIssuer
import com.spotteacher.admin.shared.auth.refreshToken.domain.TokenPair
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component // インフラ層のSpringコンポーネント
class JwtTokenIssuerAdapter(
    private val jwtProvider: JwtProvider,
    private val refreshTokenManager: RefreshTokenManager
) : TokenIssuer {
    override suspend fun issueToken(user: ActiveAdminUser): TokenPair {
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
