package com.spotteacher.admin.config.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret}") private val jwtSecretString: String,
    @Value("\${jwt.access-token-expiration-ms}") private val accessTokenExpirationMs: Long
) {
    // プロパティから読み込んだ秘密鍵を基にSecretKeyを生成
    val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretString))

    fun createAccessToken(authentication: Authentication): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpirationMs)

        val roles = authentication.authorities.map(GrantedAuthority::getAuthority)

        return Jwts.builder()
            .subject(authentication.name)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    // 他にRefreshToken生成や、トークン検証メソッドなどをここに実装
}
