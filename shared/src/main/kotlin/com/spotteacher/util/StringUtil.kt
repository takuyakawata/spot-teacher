package com.spotteacher.util

object StringUtil {
    fun String.camelToSnakeCase(): String {
        return this.replace(Regex("([a-z])([A-Z])"), "$1_$2")
            .lowercase()
    }
}
