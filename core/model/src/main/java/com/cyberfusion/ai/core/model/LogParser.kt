package com.cyberfusion.ai.core.model

object LogParser {
    data class LogEvent(
        val timestamp: Long,
        val level: String,
        val source: String,
        val message: String
    )

    fun parse(content: String): List<LogEvent> {
        if (content.isBlank()) return emptyList()
        val events = mutableListOf<LogEvent>()
        val lines = content.split('\n')
        var currentTimestamp = System.currentTimeMillis()

        lines.forEachIndexed { index, line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return@forEachIndexed

            val timestamp = extractTimestamp(trimmed) ?: currentTimestamp + index
            val level = extractLevel(trimmed)
            val source = extractSource(trimmed) ?: "unknown"
            val message = trimmed

            events.add(LogEvent(timestamp, level, source, message))
        }
        return events
    }

    private fun extractTimestamp(line: String): Long? {
        val patterns = listOf(
            Regex("""\d{4}-\d{2}-\d{2}[T ]\d{2}:\d{2}:\d{2}(\.\d+)?"""),
            Regex("""\d{2}/\w{3}/\d{4}:\d{2}:\d{2}:\d{2}"""),
            Regex("""\w{3}\s+\d{1,2}\s+\d{2}:\d{2}:\d{2}""")
        )
        for (pattern in patterns) {
            val match = pattern.find(line)
            if (match != null) {
                return try {
                    java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.US)
                        .parse(match.value)?.time
                } catch (e: Exception) {
                    null
                } ?: System.currentTimeMillis()
            }
        }
        return null
    }

    private fun extractLevel(line: String): String {
        val upper = line.uppercase()
        return when {
            upper.contains("CRITICAL") || upper.contains("FATAL") -> "CRITICAL"
            upper.contains("ERROR") || upper.contains("ERR") -> "ERROR"
            upper.contains("WARN") || upper.contains("WARNING") -> "WARNING"
            upper.contains("INFO") || upper.contains("INFORMATION") -> "INFO"
            upper.contains("DEBUG") -> "DEBUG"
            else -> "INFO"
        }
    }

    private fun extractSource(line: String): String? {
        val sourcePatterns = listOf(
            Regex("""\[([^\]]+)\]"""),
            Regex("""\s(\w+)\s*:\s""")
        )
        for (pattern in sourcePatterns) {
            val match = pattern.find(line)
            if (match != null) {
                return match.groupValues.getOrNull(1) ?: match.value.trim().removeSurrounding("[", "]")
            }
        }
        return null
    }
}
