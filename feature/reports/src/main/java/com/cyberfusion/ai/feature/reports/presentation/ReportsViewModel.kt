package com.cyberfusion.ai.feature.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.domain.usecase.report.GenerateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val generateReportUseCase: GenerateReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReportsUiState>(ReportsUiState.Idle)
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    fun generateReport() {
        _uiState.value = ReportsUiState.Generating
        viewModelScope.launch {
            when (val result = generateReportUseCase()) {
                is Result.Success -> {
                    _uiState.value = ReportsUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = ReportsUiState.Error(result.message ?: "Report generation failed")
                }
                else -> _uiState.value = ReportsUiState.Generating
            }
        }
    }

    fun resetState() {
        _uiState.value = ReportsUiState.Idle
    }
}

sealed interface ReportsUiState {
    data object Idle : ReportsUiState
    data object Generating : ReportsUiState
    data class Success(val path: String) : ReportsUiState
    data class Error(val message: String) : ReportsUiState
}
