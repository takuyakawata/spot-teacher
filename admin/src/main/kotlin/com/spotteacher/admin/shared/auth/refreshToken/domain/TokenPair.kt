package com.spotteacher.admin.shared.auth.refreshToken.domain

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
