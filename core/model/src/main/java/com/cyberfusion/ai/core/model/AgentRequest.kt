package com.cyberfusion.ai.core.model

data class AgentRequest(
    val agentType: AgentType,
    val task: String,
    val context: Map<String, String>,
    val tools: List<String>
)
