package com.cyberfusion.ai.core.model

data class AIProviderConfig(
    val providerId: String,
    val displayName: String,
    val model: String,
    val apiKey: String,
    val timeoutSeconds: Int = 30
)
