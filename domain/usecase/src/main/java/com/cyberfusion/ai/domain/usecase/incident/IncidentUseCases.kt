package com.cyberfusion.ai.domain.usecase.incident

import com.cyberfusion.ai.core.database.entity.IncidentEntity
import com.cyberfusion.ai.core.database.repository.IncidentRepository
import com.cyberfusion.ai.core.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIncidentsUseCase @Inject constructor(
    private val repository: IncidentRepository
) {
    operator fun invoke(): Flow<Result<List<IncidentEntity>>> = repository.getAllIncidents()
}

class GetIncidentByIdUseCase @Inject constructor(
    private val repository: IncidentRepository
) {
    suspend operator fun invoke(id: String): Result<IncidentEntity?> = repository.getIncidentById(id)
}

class SaveIncidentUseCase @Inject constructor(
    private val repository: IncidentRepository
) {
    suspend operator fun invoke(incident: IncidentEntity): Result<Unit> = repository.insertIncident(incident)
}
