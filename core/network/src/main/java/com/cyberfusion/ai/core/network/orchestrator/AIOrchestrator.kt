package com.cyberfusion.ai.core.network.orchestrator

import com.cyberfusion.ai.core.model.AgentRequest
import com.cyberfusion.ai.core.model.AgentResponse
import com.cyberfusion.ai.core.model.AgentType
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.network.provider.AIProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIOrchestrator @Inject constructor(
    private val providerManager: com.cyberfusion.ai.core.network.provider.AIProviderManager,
    private val toolRegistry: ToolRegistry,
    private val outputValidator: OutputValidator
) {
    suspend fun route(request: AgentRequest): Result<AgentResponse> {
        val provider = providerManager.getAvailableProvider()
            ?: return Result.Error(Exception("No AI provider available"))

        val systemPrompt = buildSystemPrompt(request.agentType)
        val toolContext = toolRegistry.buildContext(request.tools)
        val fullPrompt = buildPrompt(request.task, toolContext, request.context)

        return when (val result = provider.generateStructured(fullPrompt, "agent_response", systemPrompt)) {
            is Result.Success -> {
                val validated = outputValidator.validate(result.data)
                Result.Success(
                    AgentResponse(
                        agentType = request.agentType,
                        content = validated,
                        confidence = 70,
                        evidence = emptyList(),
                        limitations = listOf("AI-generated content requires human verification")
                    )
                )
            }
            is Result.Error -> result
            Result.Loading -> Result.Loading
        }
    }

    private fun buildSystemPrompt(agentType: AgentType): String = when (agentType) {
        AgentType.SOC -> "You are the CYBERFUSION SOC Agent. Perform evidence-driven defensive cybersecurity analysis. Separate confirmed evidence from inference. Do not invent missing facts. State confidence and limitations."
        AgentType.THREAT_INTEL -> "You are the CYBERFUSION Threat Intelligence Agent. Analyze indicators using authorized intelligence sources. Provide structured assessments with confidence levels."
        AgentType.INCIDENT -> "You are the CYBERFUSION Incident Agent. Summarize incidents, organize investigations, and provide containment and remediation recommendations."
        AgentType.GRC -> "You are the CYBERFUSION GRC Agent. Draft risk statements, identify control gaps, and explain residual risk. Label suggestions as suggestions."
        AgentType.REPORT -> "You are the CYBERFUSION Report Agent. Generate structured investigation, incident, risk, and executive reports."
    }

    private fun buildPrompt(task: String, toolContext: String, context: Map<String, String>): String {
        return buildString {
            appendLine("Task: $task")
            if (toolContext.isNotBlank()) {
                appendLine("Available tools: $toolContext")
            }
            if (context.isNotEmpty()) {
                appendLine("Context: ${context.entries.joinToString { "${it.key}: ${it.value}" }}")
            }
        }
    }
}
