package com.cyberfusion.ai.feature.grc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.database.entity.RiskEntity
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.domain.usecase.grc.GetAllRisksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrcViewModel @Inject constructor(
    private val getAllRisksUseCase: GetAllRisksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GrcUiState>(GrcUiState.Loading)
    val uiState: StateFlow<GrcUiState> = _uiState.asStateFlow()

    init {
        loadRisks()
    }

    private fun loadRisks() {
        viewModelScope.launch {
            getAllRisksUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            GrcUiState.Empty
                        } else {
                            GrcUiState.Success(result.data)
                        }
                    }
                    is Result.Error -> GrcUiState.Error(result.message ?: "Unknown error")
                    else -> GrcUiState.Loading
                }
            }
        }
    }
}

sealed interface GrcUiState {
    data object Loading : GrcUiState
    data object Empty : GrcUiState
    data class Success(val risks: List<RiskEntity>) : GrcUiState
    data class Error(val message: String) : GrcUiState
}
