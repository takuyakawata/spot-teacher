package com.spotteacher.teacher.shared.auth.domain

import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.shared.domain.Password

data class AuthUser(
    val email: EmailAddress,
    val password: Password
)
