package com.spotteacher.admin.shared.auth.domain

interface TokenIssuer {
    suspend fun issueToken(user: AuthUser): TokenPair
}
