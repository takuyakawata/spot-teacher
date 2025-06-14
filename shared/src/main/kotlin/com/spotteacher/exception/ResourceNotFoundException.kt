package com.spotteacher.exception

import kotlin.reflect.KClass

/**
 * entitiyが存在しなかった場合の例外クラス
 */
class ResourceNotFoundException(
    clazz: KClass<*>,
    params: Map<String, Any>? = null,
) : RuntimeException(
    if (params != null) {
        "Resource not found: ${clazz.simpleName}: $params"
    } else {
        "Resource not found: ${clazz.simpleName}"
    }
)
