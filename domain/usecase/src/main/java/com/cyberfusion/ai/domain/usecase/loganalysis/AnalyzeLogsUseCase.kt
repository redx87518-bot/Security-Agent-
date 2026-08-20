package com.cyberfusion.ai.domain.usecase.loganalysis

import com.cyberfusion.ai.core.model.DetectionRulesEngine
import com.cyberfusion.ai.core.model.LogParser
import com.cyberfusion.ai.core.model.NotablePattern
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.network.service.CyberFusionApi
import com.cyberfusion.ai.core.database.entity.LogEventEntity
import com.cyberfusion.ai.core.database.repository.LogEventRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class AnalyzeLogsUseCase @Inject constructor(
    private val api: CyberFusionApi,
    private val logEventRepository: LogEventRepository
) {
    suspend operator fun invoke(content: String): Result<LogAnalysisResult> {
        val events = LogParser.parse(content)
        val patterns = DetectionRulesEngine.detectPatterns(events).map { match ->
            NotablePattern(
                patternType = match.patternType,
                description = match.description,
                severity = match.severity,
                relatedEventIds = match.relatedEventIds
            )
        }
        val remoteResult = api.analyzeLogs(content)
        return when (remoteResult) {
            is Result.Success -> {
                val summary = if (remoteResult.data.summary.isNotBlank()) {
                    remoteResult.data.summary
                } else {
                    "Analyzed ${events.size} events locally with ${patterns.size} patterns detected."
                }
                persistLogEvents(events)
                Result.Success(
                    LogAnalysisResult(
                        eventCount = events.size,
                        notablePatterns = patterns,
                        summary = summary
                    )
                )
            }
            is Result.Error -> {
                persistLogEvents(events)
                Result.Success(
                    LogAnalysisResult(
                        eventCount = events.size,
                        notablePatterns = patterns,
                        summary = "Local analysis completed. Remote enrichment failed: ${remoteResult.message}"
                    )
                )
            }
            Result.Loading -> Result.Loading
        }
    }

    private suspend fun persistLogEvents(events: List<LogParser.LogEvent>) {
        events.forEach { event ->
            logEventRepository.insertLogEvent(
                LogEventEntity(
                    id = UUID.randomUUID().toString(),
                    source = event.source,
                    timestamp = event.timestamp,
                    eventType = event.level,
                    severity = event.level,
                    rawContent = event.message,
                    normalizedJson = "",
                    investigationId = null
                )
            )
        }
    }
}

data class LogAnalysisResult(
    val eventCount: Int,
    val notablePatterns: List<com.cyberfusion.ai.core.model.NotablePattern>,
    val summary: String
)
