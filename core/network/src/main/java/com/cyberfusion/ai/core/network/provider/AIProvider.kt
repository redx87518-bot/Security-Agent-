package com.cyberfusion.ai.core.network.provider

import com.cyberfusion.ai.core.model.AIProviderConfig
import com.cyberfusion.ai.core.model.Result

interface AIProvider {
    val providerId: String
    val displayName: String
    suspend fun generate(prompt: String, systemPrompt: String? = null): Result<String>
    suspend fun generateStructured(prompt: String, schema: String, systemPrompt: String? = null): Result<String>
    suspend fun testConnection(): Result<Boolean>
    suspend fun isAvailable(): Boolean
}
