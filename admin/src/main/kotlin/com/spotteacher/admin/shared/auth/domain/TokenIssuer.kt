package com.spotteacher.admin.shared.auth.domain

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser

interface TokenIssuer {
    suspend fun issueTokens(user: ActiveAdminUser): TokenPair
}
