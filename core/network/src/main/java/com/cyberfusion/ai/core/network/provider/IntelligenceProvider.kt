package com.cyberfusion.ai.core.network.provider

import com.cyberfusion.ai.core.model.AIProviderConfig
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.network.service.IndicatorAnalysisResult

interface IntelligenceProvider {
    val providerId: String
    val displayName: String
    suspend fun analyzeIndicator(value: String, type: String): Result<IndicatorAnalysisResult>
    suspend fun testConnection(): Result<Boolean>
    suspend fun isAvailable(): Boolean
}
