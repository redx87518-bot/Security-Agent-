package com.cyberfusion.ai.core.model

object DetectionRulesEngine {
    data class PatternMatch(
        val patternType: String,
        val description: String,
        val severity: String,
        val relatedEventIds: List<String>
    )

    fun detectPatterns(events: List<LogParser.LogEvent>): List<PatternMatch> {
        if (events.isEmpty()) return emptyList()
        val matches = mutableListOf<PatternMatch>()
        val errorEvents = events.filter { it.level == "ERROR" || it.level == "CRITICAL" }
        val authEvents = events.filter { it.message.contains("auth", ignoreCase = true) || it.message.contains("login", ignoreCase = true) }
        val networkEvents = events.filter { it.message.contains("connection", ignoreCase = true) || it.message.contains("network", ignoreCase = true) }

        if (errorEvents.size >= 3) {
            matches.add(
                PatternMatch(
                    patternType = "REPEATED_ERRORS",
                    description = "Detected ${errorEvents.size} error/critical events in sequence",
                    severity = "HIGH",
                    relatedEventIds = errorEvents.map { it.hashCode().toString() }
                )
            )
        }

        if (authEvents.isNotEmpty()) {
            matches.add(
                PatternMatch(
                    patternType = "AUTH_ACTIVITY",
                    description = "Detected ${authEvents.size} authentication-related events",
                    severity = if (authEvents.any { it.level == "ERROR" }) "HIGH" else "MEDIUM",
                    relatedEventIds = authEvents.map { it.hashCode().toString() }
                )
            )
        }

        if (networkEvents.isNotEmpty()) {
            matches.add(
                PatternMatch(
                    patternType = "NETWORK_ACTIVITY",
                    description = "Detected ${networkEvents.size} network-related events",
                    severity = "LOW",
                    relatedEventIds = networkEvents.map { it.hashCode().toString() }
                )
            )
        }

        return matches
    }
}
