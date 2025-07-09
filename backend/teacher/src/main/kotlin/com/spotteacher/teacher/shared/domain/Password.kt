package com.spotteacher.teacher.shared.domain

@JvmInline
value class Password(val value: String) {
    companion object {
        private const val MIN_LENGTH = 8
        private const val MAX_LENGTH = 100
    }
    init {
        require(value.length <= MAX_LENGTH) { "Password must be less than $MAX_LENGTH characters" }
        require(value.length >= MIN_LENGTH) { "Password must be greater than $MIN_LENGTH characters" }
        require(value.isNotBlank()) { "Password must not be blank" }
    }
}
