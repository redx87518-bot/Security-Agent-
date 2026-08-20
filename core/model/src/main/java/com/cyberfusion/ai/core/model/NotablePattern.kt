package com.cyberfusion.ai.core.model

data class NotablePattern(
    val patternType: String,
    val description: String,
    val severity: String,
    val relatedEventIds: List<String>
)
