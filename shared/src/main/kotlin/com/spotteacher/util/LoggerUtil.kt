package com.spotteacher.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// TODO 全体のsharedで扱う知識
val <T : Any> T.logger: Logger
    get() = LoggerFactory.getLogger(this::class.java)

// TRACE
inline fun <T> T.logTrace(message: () -> String) {
    if (this!!.logger.isTraceEnabled) {
        this.logger.trace(message())
    }
}

// DEBUG
inline fun <T> T.logDebug(message: () -> String) {
    if (this!!.logger.isDebugEnabled) {
        this.logger.debug(message())
    }
}

// INFO
inline fun <T> T.logInfo(message: () -> String) {
    if (this!!.logger.isInfoEnabled) {
        this.logger.info(message())
    }
}

// WARN
inline fun <T> T.logWarn(message: () -> String) {
    if (this!!.logger.isWarnEnabled) {
        this.logger.warn(message())
    }
}

// ERROR
inline fun <T> T.logError(message: () -> String, throwable: Throwable? = null) {
    if (this!!.logger.isErrorEnabled) {
        this.logger.error(message(), throwable)
    }
}

// タグ付きログ
inline fun <T> T.logWithTag(tag: String, level: LogLevel = LogLevel.INFO, message: () -> String, throwable: Throwable? = null) {
    val logMessage = "[$tag] ${message()}"
    when (level) {
        LogLevel.TRACE -> logTrace { logMessage }
        LogLevel.DEBUG -> logDebug { logMessage }
        LogLevel.INFO -> logInfo { logMessage }
        LogLevel.WARN -> logWarn { logMessage }
        LogLevel.ERROR -> logError({ logMessage }, throwable)
    }
}

enum class LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR
}
