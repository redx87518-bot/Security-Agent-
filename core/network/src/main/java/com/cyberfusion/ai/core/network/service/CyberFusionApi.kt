package com.cyberfusion.ai.core.network.service

import com.cyberfusion.ai.core.common.AppError
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.model.Severity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class IndicatorAnalysisResult(
    val indicator: String,
    val type: String,
    val severity: String,
    val confidence: Int,
    val sources: List<IntelligenceSource>,
    val threatScore: Int,
    val evidence: List<String>
)

data class IntelligenceSource(
    val name: String,
    val result: String,
    val timestamp: Long?
)

data class LogAnalysisResult(
    val eventCount: Int,
    val notablePatterns: List<NotablePattern>,
    val summary: String
)

data class NotablePattern(
    val patternType: String,
    val description: String,
    val severity: String,
    val relatedEventIds: List<String>
)

interface CyberFusionApi {
    suspend fun analyzeIndicator(value: String, type: String): Result<IndicatorAnalysisResult>
    suspend fun analyzeLogs(content: String): Result<LogAnalysisResult>
}

class HttpCyberFusionApi @Inject constructor(
    private val baseUrl: String,
    private val apiKey: String
) : CyberFusionApi {
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
                        AppError.NetworkError(response.code, "API error: ${response.message}")
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

    override suspend fun analyzeLogs(content: String): Result<LogAnalysisResult> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = JSONObject().apply {
                    put("content", content)
                }.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$baseUrl/v1/analyze-logs")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext Result.Error(
                        AppError.NetworkError(response.code, "API error: ${response.message}")
                    )
                }

                val body = response.body?.string() ?: ""
                val json = JSONObject(body)
                val patterns = mutableListOf<NotablePattern>()
                val patternsArray = json.optJSONArray("notablePatterns")
                for (i in 0 until (patternsArray?.length() ?: 0)) {
                    val patternObj = patternsArray?.optJSONObject(i)
                    patterns.add(
                        NotablePattern(
                            patternType = patternObj?.optString("patternType") ?: "unknown",
                            description = patternObj?.optString("description") ?: "",
                            severity = patternObj?.optString("severity") ?: "INFORMATIONAL",
                            relatedEventIds = List(patternObj?.optJSONArray("relatedEventIds")?.length() ?: 0) { j ->
                                patternObj?.optJSONArray("relatedEventIds")?.optString(j) ?: ""
                            }
                        )
                    )
                }

                Result.Success(
                    LogAnalysisResult(
                        eventCount = json.optInt("eventCount", 0),
                        notablePatterns = patterns,
                        summary = json.optString("summary", "No summary available")
                    )
                )
            } catch (e: Exception) {
                Result.Error(AppError.NetworkError(-1, e.message ?: "Network request failed"), e.message)
            }
        }
    }
}
