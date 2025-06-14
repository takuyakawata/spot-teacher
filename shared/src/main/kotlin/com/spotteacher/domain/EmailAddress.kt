package com.spotteacher.domain

import java.util.regex.Pattern

@JvmInline
value class EmailAddress(val value: String) {
    companion object {
        // Exchange の最大長を踏襲
        const val MAX_LENGTH = 254
    }

    init {
        require(value.isNotBlank()) {
            "Recipient must not be blank"
        }
        require(value.length <= MAX_LENGTH) {
            "Recipient must be less than $MAX_LENGTH"
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        require(Pattern.matches(emailRegex, value)) {
            "Recipient must match an email"
        }
    }
}
