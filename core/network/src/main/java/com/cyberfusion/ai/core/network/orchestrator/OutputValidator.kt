package com.cyberfusion.ai.core.network.orchestrator

import com.cyberfusion.ai.core.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutputValidator @Inject constructor() {
    fun validate(output: String): String {
        val trimmed = output.trim()
        if (trimmed.isBlank()) return "No output generated"
        if (trimmed.length > 10000) return trimmed.take(10000) + "\n... (truncated)"
        return trimmed
    }
}
