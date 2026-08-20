package com.cyberfusion.ai.core.network.orchestrator

import com.cyberfusion.ai.core.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRegistry @Inject constructor() {
    private val tools = mutableMapOf<String, suspend (Map<String, String>) -> Result<String>>()

    fun registerTool(name: String, handler: suspend (Map<String, String>) -> Result<String>) {
        tools[name] = handler
    }

    suspend fun execute(name: String, parameters: Map<String, String>): Result<String> {
        val handler = tools[name] ?: return Result.Error(Exception("Unknown tool: $name"))
        return handler(parameters)
    }

    fun buildContext(toolNames: List<String>): String {
        return toolNames.joinToString { it }
    }
}
