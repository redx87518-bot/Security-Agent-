package com.cyberfusion.ai.feature.incident.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.database.entity.IncidentEntity
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.domain.usecase.incident.GetAllIncidentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidentViewModel @Inject constructor(
    private val getAllIncidentsUseCase: GetAllIncidentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<IncidentUiState>(IncidentUiState.Loading)
    val uiState: StateFlow<IncidentUiState> = _uiState.asStateFlow()

    init {
        loadIncidents()
    }

    private fun loadIncidents() {
        viewModelScope.launch {
            getAllIncidentsUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            IncidentUiState.Empty
                        } else {
                            IncidentUiState.Success(result.data)
                        }
                    }
                    is Result.Error -> IncidentUiState.Error(result.message ?: "Unknown error")
                    else -> IncidentUiState.Loading
                }
            }
        }
    }
}

sealed interface IncidentUiState {
    data object Loading : IncidentUiState
    data object Empty : IncidentUiState
    data class Success(val incidents: List<IncidentEntity>) : IncidentUiState
    data class Error(val message: String) : IncidentUiState
}
