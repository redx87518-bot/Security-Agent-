package com.cyberfusion.ai.core.common

object ErrorHandler {
    fun logError(error: Throwable, tag: String = "CyberFusion") {
        android.util.Log.e(tag, "Unhandled error", error)
    }

    fun mapThrowable(throwable: Throwable): AppError = when (throwable) {
        is AppError -> throwable
        else -> AppError.Unknown(throwable.message ?: "Unknown error")
    }
}
