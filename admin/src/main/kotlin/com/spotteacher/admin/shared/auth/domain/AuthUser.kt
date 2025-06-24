package com.spotteacher.admin.shared.auth.domain

import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.domain.EmailAddress

data class AuthUser(
    val email: EmailAddress,
    val password: Password
)
