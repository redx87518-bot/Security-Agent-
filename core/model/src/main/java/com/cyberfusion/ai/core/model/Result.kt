package com.cyberfusion.ai.core.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Throwable, val message: String? = null) : Result<T>()
    data class Loading<T>(val message: String? = null) : Result<T>()
    object Unit : Result<Unit>()
}
