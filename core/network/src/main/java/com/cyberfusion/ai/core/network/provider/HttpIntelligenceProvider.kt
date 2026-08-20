package com.cyberfusion.ai.core.network.provider

import com.cyberfusion.ai.core.model.AppError
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.network.service.IndicatorAnalysisResult
import com.cyberfusion.ai.core.network.service.IntelligenceSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HttpIntelligenceProvider @Inject constructor(
    override val providerId: String,
    override val displayName: String,
    private val apiKey: String,
    private val baseUrl: String
) : IntelligenceProvider {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    override suspend fun analyzeIndicator(value: String, type: String): Result<IndicatorAnalysisResult> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = JSONObject().apply {
                    put("indicator", value)
                    put("type", type)
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$baseUrl/v1/analyze")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext Result.Error(
                        AppError.NetworkError(response.code, "Intelligence provider error: ${response.message}")
                    )
                }

                val body = response.body?.string() ?: ""
                val json = JSONObject(body)
                val sources = mutableListOf<IntelligenceSource>()
                val sourcesArray = json.optJSONArray("sources")
                for (i in 0 until (sourcesArray?.length() ?: 0)) {
                    val sourceObj = sourcesArray?.optJSONObject(i)
                    sources.add(
                        IntelligenceSource(
                            name = sourceObj?.optString("name") ?: "Unknown",
                            result = sourceObj?.optString("result") ?: "",
                            timestamp = sourceObj?.optLong("timestamp")
                        )
                    )
                }

                Result.Success(
                    IndicatorAnalysisResult(
                        indicator = value,
                        type = type,
                        severity = json.optString("severity", "UNKNOWN"),
                        confidence = json.optInt("confidence", 0),
                        sources = sources,
                        threatScore = json.optInt("threatScore", 0),
                        evidence = List(json.optJSONArray("evidence")?.length() ?: 0) { i ->
                            json.optJSONArray("evidence")?.optString(i) ?: ""
                        }
                    )
                )
            } catch (e: Exception) {
                Result.Error(AppError.NetworkError(-1, e.message ?: "Network request failed"), e.message)
            }
        }
    }

    override suspend fun testConnection(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("$baseUrl/health")
                    .get()
                    .build()

                val response = client.newCall(request).execute()
                Result.Success(response.isSuccessful)
            } catch (e: Exception) {
                Result.Error(AppError.NetworkError(-1, e.message ?: "Connection test failed"), e.message)
            }
        }
    }

    override suspend fun isAvailable(): Boolean {
        return apiKey.isNotBlank()
    }
}
