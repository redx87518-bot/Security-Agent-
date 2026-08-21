package com.cyberfusion.ai.core.network.provider

import com.cyberfusion.ai.core.model.AIProviderConfig
import com.cyberfusion.ai.core.common.AppError
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.network.service.CyberFusionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HttpAIProvider @Inject constructor(
    private val config: AIProviderConfig,
    private val api: CyberFusionApi
) : AIProvider {
    override val providerId: String = config.providerId
    override val displayName: String = config.displayName

    private val client = OkHttpClient.Builder()
        .connectTimeout(config.timeoutSeconds.toLong(), TimeUnit.SECONDS)
        .readTimeout(config.timeoutSeconds.toLong(), TimeUnit.SECONDS)
        .build()

    override suspend fun generate(prompt: String, systemPrompt: String?): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = JSONObject().apply {
                    put("model", config.model)
                    put("prompt", if (systemPrompt != null) "$systemPrompt\n\n$prompt" else prompt)
                    put("max_tokens", 1024)
                    put("temperature", 0.3)
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("${config.providerId}/v1/completions")
                    .addHeader("Authorization", "Bearer ${config.apiKey}")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext Result.Error(
                        AppError.AIProviderError(config.providerId, "HTTP ${response.code}: ${response.message}")
                    )
                }

                val body = response.body?.string() ?: ""
                val json = JSONObject(body)
                val text = json.optJSONArray("choices")
                    ?.optJSONObject(0)
                    ?.optString("text")
                    ?: "No response content"

                Result.Success(text.trim())
            } catch (e: Exception) {
                Result.Error(AppError.AIProviderError(config.providerId, e.message ?: "Request failed"), e.message)
            }
        }
    }

    override suspend fun generateStructured(prompt: String, schema: String, systemPrompt: String?): Result<String> {
        return generate(prompt, systemPrompt)
    }

    override suspend fun testConnection(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = JSONObject().apply {
                    put("model", config.model)
                    put("prompt", "test")
                    put("max_tokens", 1)
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("${config.providerId}/v1/completions")
                    .addHeader("Authorization", "Bearer ${config.apiKey}")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                Result.Success(response.isSuccessful)
            } catch (e: Exception) {
                Result.Error(AppError.AIProviderError(config.providerId, e.message ?: "Connection test failed"), e.message)
            }
        }
    }

    override suspend fun isAvailable(): Boolean {
        return config.apiKey.isNotBlank()
    }
}
