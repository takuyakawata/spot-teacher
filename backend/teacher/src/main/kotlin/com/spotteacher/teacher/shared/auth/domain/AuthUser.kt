package com.spotteacher.teacher.shared.auth.domain

import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.shared.domain.Password
import com.spotteacher.util.Identity

data class AuthUser(
    val id: AuthUserId,
    val email: EmailAddress,
    val password: Password
)

class AuthUserId(override val value: Long) : Identity<Long>(value)
