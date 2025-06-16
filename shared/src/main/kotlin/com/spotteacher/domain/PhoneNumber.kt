package com.spotteacher.domain

@JvmInline
value class PhoneNumber(val value: String){
    companion object {
        private const val MAX_LENGTH = 12
    }
    init {
        require(value.length <= MAX_LENGTH) {
            "Phone number must be $MAX_LENGTH digits"
        }
    }
}
