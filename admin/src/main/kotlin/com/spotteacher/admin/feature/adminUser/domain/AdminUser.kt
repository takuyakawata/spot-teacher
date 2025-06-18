package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.domain.EmailAddress
import com.spotteacher.util.Identity

sealed interface AdminUser {
    val id: AdminUserId
    val firstName: AdminUserName
    val lastName: AdminUserName
}

data class ActiveAdminUser(
    override val id: AdminUserId,
    override val firstName: AdminUserName,
    override val lastName: AdminUserName,
    val email: EmailAddress,
    val password: Password,
) : AdminUser {
    companion object {
        fun create(
            firstName: AdminUserName,
            lastName: AdminUserName,
            email: EmailAddress,
            password: Password,
        ) = ActiveAdminUser(
            id = AdminUserId(0),
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
        )
    }
}

/**
 * Represents an inactive admin user.(使用停止中のユーザー）
 *
 * @property id The unique identifier for the admin user.
 * @property firstName The first name of the admin user.
 * @property lastName The last name of the admin user.
 */
data class InActiveAdminUser(
    override val id: AdminUserId,
    override val firstName: AdminUserName,
    override val lastName: AdminUserName,
) : AdminUser {
    companion object {
        fun create(
            firstName: AdminUserName,
            lastName: AdminUserName,
        ) = InActiveAdminUser(
            id = AdminUserId(0),
            firstName = firstName,
            lastName = lastName,
        )
    }
}

// Entity のIDは
class AdminUserId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class AdminUserName(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
    }
    init {
        require(value.length <= MAX_LENGTH) {
            "AdminName must be less than $MAX_LENGTH characters"
        }
    }
}

@JvmInline
value class Password(val value: String)
