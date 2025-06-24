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
    val email: EmailAddress
) : AdminUser {
    companion object {
        fun create(
            firstName: AdminUserName,
            lastName: AdminUserName,
            email: EmailAddress,
        ) = ActiveAdminUser(
            id = AdminUserId(0),
            firstName = firstName,
            lastName = lastName,
            email = email,
        )
    }
}

fun ActiveAdminUser.changePassword(password: Password) = ActiveAdminUser(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
)

fun ActiveAdminUser.toInActiveAdminUser() = InActiveAdminUser(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
)

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

fun InActiveAdminUser.toActiveAdminUser(email: EmailAddress, password: Password) = ActiveAdminUser(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    email = email,
)

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

data class AdminUserError(
    val code: AdminUserErrorCode,
    val message: String
)

enum class AdminUserErrorCode {
    ADMIN_USER_NOT_FOUND,
    ADMIN_USER_ALREADY_EXISTS,
}
