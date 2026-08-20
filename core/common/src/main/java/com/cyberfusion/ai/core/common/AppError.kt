package com.cyberfusion.ai.core.common

sealed class AppError : Exception() {
    data class NetworkError(val code: Int, override val message: String) : AppError()
    data class DatabaseError(override val message: String) : AppError()
    data class ValidationError(val field: String, override val message: String) : AppError()
    data class AIProviderError(val provider: String, override val message: String) : AppError()
    data object Unauthorized : AppError()
    data object NotFound : AppError()
    data object Timeout : AppError()
    data class Unknown(override val message: String) : AppError()
}
