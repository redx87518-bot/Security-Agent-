package com.cyberfusion.ai.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.network.provider.IntelligenceProvider
import com.cyberfusion.ai.core.security.SecurePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val securePreferences: SecurePreferences,
    private val intelligenceProvider: IntelligenceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val providerId = securePreferences.aiProvider.first()
            val isAvailable = intelligenceProvider.isAvailable()
            _uiState.value = SettingsUiState.Success(
                configuredProvider = providerId ?: "Not configured",
                isAvailable = isAvailable
            )
        }
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val configuredProvider: String,
        val isAvailable: Boolean
    ) : SettingsUiState
    data class Error(val message: String) : SettingsUiState
}
