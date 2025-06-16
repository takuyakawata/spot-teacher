package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.domain.EmailAddress
import com.spotteacher.util.Identity

sealed interface AdminUser{
    val id: AdminUserId
    val firstName: AdminName
    val lastName: AdminName
}

data class ActiveAdminUser(
    override val id: AdminUserId,
    override val firstName: AdminName,
    override val lastName: AdminName,
    val email: EmailAddress,
    val password: Password,
) : AdminUser {
    companion object{
        fun create(
            firstName: AdminName,
            lastName: AdminName,
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
    override val firstName: AdminName,
    override val lastName: AdminName,
) : AdminUser {
    companion object{
       fun create(
           firstName: AdminName,
           lastName: AdminName,
       ) = InActiveAdminUser(
           id = AdminUserId(0),
           firstName = firstName,
           lastName = lastName,
       )
    }
}

//Entity のIDは
class AdminUserId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class AdminName(val value: String){
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

