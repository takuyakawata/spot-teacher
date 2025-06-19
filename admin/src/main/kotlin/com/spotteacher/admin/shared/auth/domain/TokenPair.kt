package com.spotteacher.admin.shared.auth.domain

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
