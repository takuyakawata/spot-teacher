package com.spotteacher.admin.feature.adminUser.domain

import com.spotteacher.util.Identity

data class AdminUser(
    val id: AdminUserId,
    val firstName: AdminName,
    val lastName: AdminName,
    val email: Email,
    val password: Password,
    val isActive: Boolean
)

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
value class Email(val value: String){
    companion object {
        const val MAX_LENGTH = 500
    }
    init {
        require(value.length <= MAX_LENGTH) {
            "Email must be less than $MAX_LENGTH characters"
        }

        // メールの形式になっているか
        require(value.matches(Regex("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))) {
            "Invalid email format"
        }
    }
}

@JvmInline
value class Password(val value: String)

