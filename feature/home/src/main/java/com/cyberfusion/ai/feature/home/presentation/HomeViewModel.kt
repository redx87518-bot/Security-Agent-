package com.cyberfusion.ai.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfusion.ai.core.database.repository.AlertRepository
import com.cyberfusion.ai.core.database.repository.IncidentRepository
import com.cyberfusion.ai.core.database.repository.IndicatorRepository
import com.cyberfusion.ai.core.database.repository.InvestigationRepository
import com.cyberfusion.ai.core.database.repository.RiskRepository
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.domain.usecase.investigation.GetAllInvestigationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllInvestigationsUseCase: GetAllInvestigationsUseCase,
    private val investigationRepository: InvestigationRepository,
    private val incidentRepository: IncidentRepository,
    private val alertRepository: AlertRepository,
    private val indicatorRepository: IndicatorRepository,
    private val riskRepository: RiskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                investigationRepository.getAllInvestigations(),
                incidentRepository.getAllIncidents(),
                alertRepository.getAllAlerts(),
                indicatorRepository.getAllIndicators(),
                riskRepository.getAllRisks()
            ) { inv, inc, alerts, inds, risks ->
                val investigations = (inv as? Result.Success)?.data ?: emptyList()
                val incidents = (inc as? Result.Success)?.data ?: emptyList()
                val alertsList = (alerts as? Result.Success)?.data ?: emptyList()
                val indicators = (inds as? Result.Success)?.data ?: emptyList()
                val risksList = (risks as? Result.Success)?.data ?: emptyList()

                val criticalFindings = alertsList.count { 
                    it.severity.equals("HIGH", true) || it.severity.equals("CRITICAL", true) 
                } + incidents.count { 
                    it.severity.equals("HIGH", true) || it.severity.equals("CRITICAL", true) 
                }
                val openIncidents = incidents.count { 
                    it.status.equals("NEW", true) || it.status.equals("INVESTIGATING", true) 
                }

                HomeUiState.Success(
                    investigations = investigations,
                    openIncidents = openIncidents,
                    criticalFindings = criticalFindings
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val investigations: List<com.cyberfusion.ai.core.database.entity.InvestigationEntity>,
        val openIncidents: Int,
        val criticalFindings: Int
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
