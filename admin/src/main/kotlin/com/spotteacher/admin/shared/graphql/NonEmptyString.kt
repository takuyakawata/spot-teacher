package com.spotteacher.admin.shared.graphql

import org.valiktor.functions.isNotBlank
import org.valiktor.validate

@JvmInline
value class NonEmptyString(val value: String) {
    init {
        try {
            validate(this) {
                validate(NonEmptyString::value).isNotBlank()
            }
        } catch (e: Exception) {
            println("NonEmptyString エラー　error: ${e.message}")
            throw e
        }
    }
}

fun String.toNonEmptyStringOrNull(): NonEmptyString? = if (this.isNotBlank()) NonEmptyString(this) else null
