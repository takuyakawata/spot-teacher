package com.spotteacher.admin.shared.auth.refreshToken.domain

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser

interface TokenIssuer {
    suspend fun issueToken(user: ActiveAdminUser): TokenPair
}
