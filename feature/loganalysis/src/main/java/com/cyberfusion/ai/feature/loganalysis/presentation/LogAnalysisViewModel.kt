package com.cyberfusion.ai.feature.loganalysis.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.domain.usecase.loganalysis.AnalyzeLogsUseCase
import com.cyberfusion.ai.domain.usecase.loganalysis.LogAnalysisResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogAnalysisViewModel @Inject constructor(
    private val analyzeLogsUseCase: AnalyzeLogsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogAnalysisUiState>(LogAnalysisUiState.Idle)
    val uiState: StateFlow<LogAnalysisUiState> = _uiState.asStateFlow()

    var selectedFileUri: String? = null
        private set

    fun setSelectedFileUri(uri: String?) {
        selectedFileUri = uri
    }

    fun analyzeLogs(content: String) {
        if (content.isBlank()) {
            _uiState.value = LogAnalysisUiState.Error("Log content cannot be empty")
            return
        }
        _uiState.value = LogAnalysisUiState.Loading
        viewModelScope.launch {
            when (val result = analyzeLogsUseCase(content)) {
                is Result.Success -> {
                    _uiState.value = LogAnalysisUiState.Completed(
                        eventCount = result.data.eventCount,
                        notablePatterns = result.data.notablePatterns,
                        summary = result.data.summary
                    )
                }
                is Result.Error -> {
                    _uiState.value = LogAnalysisUiState.Failed(result.message ?: "Analysis failed")
                }
                else -> _uiState.value = LogAnalysisUiState.Loading
            }
        }
    }

    fun resetState() {
        _uiState.value = LogAnalysisUiState.Idle
        selectedFileUri = null
    }
}

sealed interface LogAnalysisUiState {
    data object Idle : LogAnalysisUiState
    data object Loading : LogAnalysisUiState
    data object Offline : LogAnalysisUiState
    data class Completed(
        val eventCount: Int,
        val notablePatterns: List<com.cyberfusion.ai.core.model.NotablePattern>,
        val summary: String
    ) : LogAnalysisUiState
    data class PartialResult(
        val eventCount: Int,
        val notablePatterns: List<com.cyberfusion.ai.core.model.NotablePattern>,
        val summary: String
    ) : LogAnalysisUiState
    data class Failed(val message: String) : LogAnalysisUiState
    data class Error(val message: String) : LogAnalysisUiState
}
