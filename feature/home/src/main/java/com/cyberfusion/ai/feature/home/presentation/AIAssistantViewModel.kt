package com.cyberfusion.ai.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.model.AgentRequest
import com.cyberfusion.ai.core.model.AgentType
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.network.orchestrator.AIOrchestrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIAssistantViewModel @Inject constructor(
    private val orchestrator: AIOrchestrator
) : ViewModel() {

    private val _uiState = MutableStateFlow<AIAssistantUiState>(AIAssistantUiState.Idle)
    val uiState: StateFlow<AIAssistantUiState> = _uiState.asStateFlow()

    fun sendQuery(query: String) {
        if (query.isBlank()) return
        _uiState.value = AIAssistantUiState.Loading
        viewModelScope.launch {
            val result = orchestrator.route(
                AgentRequest(
                    agentType = AgentType.SOC,
                    task = query,
                    context = emptyMap(),
                    tools = emptyList()
                )
            )
            when (result) {
                is Result.Success -> {
                    _uiState.value = AIAssistantUiState.Success(
                        response = result.data.content,
                        confidence = result.data.confidence,
                        limitations = result.data.limitations
                    )
                }
                is Result.Error -> {
                    _uiState.value = AIAssistantUiState.Error(result.message ?: "Unknown error")
                }
                Result.Loading -> _uiState.value = AIAssistantUiState.Loading
            }
        }
    }
}

sealed interface AIAssistantUiState {
    data object Idle : AIAssistantUiState
    data object Loading : AIAssistantUiState
    data class Success(val response: String, val confidence: Int, val limitations: List<String>) : AIAssistantUiState
    data class Error(val message: String) : AIAssistantUiState
}
