package com.cyberfusion.ai.domain.usecase.intelligence

import com.cyberfusion.ai.core.database.entity.IndicatorEntity
import com.cyberfusion.ai.core.database.repository.IndicatorRepository
import com.cyberfusion.ai.core.model.IocValidator
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.model.ThreatScoreEngine
import com.cyberfusion.ai.core.model.ThreatScoreResult
import com.cyberfusion.ai.core.network.provider.IntelligenceProvider
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class AnalyzeIndicatorUseCase @Inject constructor(
    private val provider: IntelligenceProvider,
    private val indicatorRepository: IndicatorRepository
) {
    suspend operator fun invoke(rawInput: String): Result<Pair<com.cyberfusion.ai.core.model.IndicatorType, ThreatScoreResult?>> {
        val validation = IocValidator.validate(rawInput)
        return when (validation) {
            is IocValidator.ValidationResult.Valid -> {
                val analysisResult = provider.analyzeIndicator(validation.normalizedValue, validation.type.name)
                when (analysisResult) {
                    is Result.Success -> {
                        val score = ThreatScoreEngine.calculateScore(
                            sourceCount = analysisResult.data.sources.size,
                            maliciousClassifications = if (analysisResult.data.severity == "HIGH" || analysisResult.data.severity == "CRITICAL") 1 else 0,
                            suspiciousClassifications = if (analysisResult.data.severity == "MEDIUM" || analysisResult.data.severity == "LOW") 1 else 0,
                            recencyScore = 10,
                            repeatedLocalObservations = 0,
                            analystConfirmed = 0,
                            relatedIndicators = 0
                        )
                        val indicator = IndicatorEntity(
                            id = UUID.randomUUID().toString(),
                            value = validation.normalizedValue,
                            type = validation.type.name,
                            severity = score.severity.name,
                            confidence = score.confidence,
                            threatScore = score.score,
                            sourceCount = analysisResult.data.sources.size,
                            createdAt = System.currentTimeMillis(),
                            investigationId = null
                        )
                        indicatorRepository.insertIndicator(indicator)
                        Result.Success(validation.type to score)
                    }
                    is Result.Error -> Result.Error(analysisResult.exception, analysisResult.message)
                    Result.Loading -> Result.Loading
                }
            }
            is IocValidator.ValidationResult.Invalid -> Result.Error(Exception(validation.reason))
        }
    }
}
