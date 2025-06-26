package com.spotteacher.exception

/**
 * 認証に失敗した場合の例外クラス
 */
class AuthenticationFailedException(
    message: String = "Authentication failed"
) : RuntimeException(message)
