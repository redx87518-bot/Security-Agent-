package com.cyberfusion.ai.core.model

data class AgentResponse(
    val agentType: AgentType,
    val content: String,
    val confidence: Int,
    val evidence: List<String>,
    val limitations: List<String>
)
