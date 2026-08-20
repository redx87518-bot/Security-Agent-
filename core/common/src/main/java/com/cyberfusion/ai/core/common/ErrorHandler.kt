package com.cyberfusion.ai.core.common

import timber.log.Timber

object ErrorHandler {
    fun logError(error: Throwable, tag: String = "CyberFusion") {
        Timber.e(error, "[$tag] Unhandled error")
    }

    fun mapThrowable(throwable: Throwable): AppError = when (throwable) {
        is AppError -> throwable
        else -> AppError.Unknown(throwable.message ?: "Unknown error")
    }
}
