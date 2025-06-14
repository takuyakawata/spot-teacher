package com.spotteacher.admin.shared

import kotlin.hashCode
import kotlin.jvm.java
import kotlin.toString

open class Identity<T> (
    open val value: T
) {
    final override fun toString(): String {
        return value.toString()
    }

    final override fun equals(other: Any?): Boolean {
        if (other is Identity<*> && this::class.java.name == other::class.java.name) {
            return value == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
