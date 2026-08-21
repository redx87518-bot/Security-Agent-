package com.cyberfusion.ai.feature.intelligence.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.model.ThreatScoreEngine
import com.cyberfusion.ai.core.model.ThreatScoreResult
import com.cyberfusion.ai.core.network.provider.IntelligenceProvider
import com.cyberfusion.ai.domain.usecase.intelligence.AnalyzeIndicatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntelligenceViewModel @Inject constructor(
    private val analyzeIndicatorUseCase: AnalyzeIndicatorUseCase,
    private val intelligenceProvider: IntelligenceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<IntelligenceUiState>(IntelligenceUiState.Idle)
    val uiState: StateFlow<IntelligenceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (!intelligenceProvider.isAvailable()) {
                _uiState.value = IntelligenceUiState.NotConfigured
            }
        }
    }

    fun analyzeIndicator(rawInput: String) {
        if (rawInput.isBlank()) {
            _uiState.value = IntelligenceUiState.Error("Please enter an indicator")
            return
        }

        viewModelScope.launch {
            _uiState.value = IntelligenceUiState.Loading
            when (val result = analyzeIndicatorUseCase(rawInput)) {
                is Result.Success -> {
                    val (type, score) = result.data
                    _uiState.value = IntelligenceUiState.Success(
                        indicator = rawInput.trim(),
                        type = type,
                        severity = score?.severity?.name ?: "UNKNOWN",
                        confidence = score?.confidence ?: 0,
                        threatScore = score ?: ThreatScoreEngine.calculateScore(0,0,0,0,0,0,0)
                    )
                }
                is Result.Error -> {
                    _uiState.value = IntelligenceUiState.Error(result.message ?: "Analysis failed")
                }
                else -> _uiState.value = IntelligenceUiState.Loading
            }
        }
    }

    fun resetState() {
        _uiState.value = if (!intelligenceProvider.isAvailable()) {
            IntelligenceUiState.NotConfigured
        } else {
            IntelligenceUiState.Idle
        }
    }
}

sealed interface IntelligenceUiState {
    data object Idle : IntelligenceUiState
    data object NotConfigured : IntelligenceUiState
    data object Loading : IntelligenceUiState
    data class Success(
        val indicator: String,
        val type: com.cyberfusion.ai.core.model.IndicatorType,
        val severity: String,
        val confidence: Int,
        val threatScore: ThreatScoreResult
    ) : IntelligenceUiState
    data class Error(val message: String) : IntelligenceUiState
}
