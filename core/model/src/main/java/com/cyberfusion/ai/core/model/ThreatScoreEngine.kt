package com.cyberfusion.ai.core.model

object ThreatScoreEngine {
    fun calculateScore(
        sourceCount: Int,
        maliciousClassifications: Int,
        suspiciousClassifications: Int,
        recencyScore: Int,
        repeatedLocalObservations: Int,
        analystConfirmed: Int,
        relatedIndicators: Int
    ): ThreatScoreResult {
        val sourceWeight = when {
            sourceCount >= 5 -> 25
            sourceCount >= 3 -> 20
            sourceCount >= 1 -> 10
            else -> 0
        }
        val classificationWeight = (maliciousClassifications * 15) + (suspiciousClassifications * 5)
        val recencyWeight = recencyScore.coerceIn(0, 15)
        val localWeight = (repeatedLocalObservations * 10).coerceIn(0, 20)
        val analystWeight = (analystConfirmed * 20).coerceIn(0, 20)
        val relatedWeight = (relatedIndicators * 5).coerceIn(0, 10)

        val rawScore = sourceWeight + classificationWeight + recencyWeight + localWeight + analystWeight + relatedWeight
        val score = rawScore.coerceIn(0, 100)

        val severity = when {
            score >= 80 -> Severity.CRITICAL
            score >= 60 -> Severity.HIGH
            score >= 40 -> Severity.MEDIUM
            score >= 20 -> Severity.LOW
            else -> Severity.INFORMATIONAL
        }

        return ThreatScoreResult(
            score = score,
            severity = severity,
            confidence = when {
                score >= 80 && sourceCount >= 3 -> 90
                score >= 60 && sourceCount >= 2 -> 75
                score >= 40 && sourceCount >= 1 -> 60
                else -> 40
            },
            contributors = mapOf(
                "sourceCount" to sourceWeight,
                "classificationWeight" to classificationWeight,
                "recencyWeight" to recencyWeight,
                "localWeight" to localWeight,
                "analystWeight" to analystWeight,
                "relatedWeight" to relatedWeight
            )
        )
    }
}

data class ThreatScoreResult(
    val score: Int,
    val severity: Severity,
    val confidence: Int,
    val contributors: Map<String, Int>
)
