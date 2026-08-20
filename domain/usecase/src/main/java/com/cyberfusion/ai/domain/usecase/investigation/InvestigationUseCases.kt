package com.cyberfusion.ai.domain.usecase.investigation

import com.cyberfusion.ai.core.database.entity.InvestigationEntity
import com.cyberfusion.ai.core.database.repository.InvestigationRepository
import com.cyberfusion.ai.core.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllInvestigationsUseCase @Inject constructor(
    private val repository: InvestigationRepository
) {
    operator fun invoke(): Flow<Result<List<InvestigationEntity>>> {
        return repository.getAllInvestigations()
    }
}

class GetInvestigationByIdUseCase @Inject constructor(
    private val repository: InvestigationRepository
) {
    suspend operator fun invoke(id: String): Result<InvestigationEntity?> {
        return repository.getInvestigationById(id)
    }
}

class SaveInvestigationUseCase @Inject constructor(
    private val repository: InvestigationRepository
) {
    suspend operator fun invoke(investigation: InvestigationEntity): Result<Unit> {
        return repository.insertInvestigation(investigation)
    }
}
