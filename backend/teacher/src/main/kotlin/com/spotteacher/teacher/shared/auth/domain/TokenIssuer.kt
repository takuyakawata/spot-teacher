package com.spotteacher.teacher.shared.auth.domain

interface TokenIssuer {
    suspend fun issueToken(user: AuthUser): TokenPair
}
