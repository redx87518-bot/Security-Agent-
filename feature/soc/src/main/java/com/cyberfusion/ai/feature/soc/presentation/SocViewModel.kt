package com.cyberfusion.ai.feature.soc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.database.dao.CyberFusionDao
import com.cyberfusion.ai.core.database.entity.AlertEntity
import com.cyberfusion.ai.core.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocViewModel @Inject constructor(
    private val dao: CyberFusionDao
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<AlertEntity>>(emptyList())
    val alerts: StateFlow<List<AlertEntity>> = _alerts.asStateFlow()

    private val _uiState = MutableStateFlow<SocUiState>(SocUiState.Loading)
    val uiState: StateFlow<SocUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        viewModelScope.launch {
            try {
                dao.getAllAlerts().collect { alertList ->
                    _alerts.value = alertList
                    _uiState.value = SocUiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = SocUiState.Error(e.message ?: "Failed to load alerts")
            }
        }
    }
}

sealed interface SocUiState {
    data object Loading : SocUiState
    data object Success : SocUiState
    data class Error(val message: String) : SocUiState
}
