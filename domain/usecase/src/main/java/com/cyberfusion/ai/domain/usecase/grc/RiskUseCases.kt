package com.cyberfusion.ai.domain.usecase.grc

import com.cyberfusion.ai.core.database.entity.RiskEntity
import com.cyberfusion.ai.core.database.repository.RiskRepository
import com.cyberfusion.ai.core.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRisksUseCase @Inject constructor(
    private val repository: RiskRepository
) {
    operator fun invoke(): Flow<Result<List<RiskEntity>>> = repository.getAllRisks()
}

class GetRiskByIdUseCase @Inject constructor(
    private val repository: RiskRepository
) {
    suspend operator fun invoke(id: String): Result<RiskEntity?> = repository.getRiskById(id)
}

class SaveRiskUseCase @Inject constructor(
    private val repository: RiskRepository
) {
    suspend operator fun invoke(risk: RiskEntity): Result<Unit> = repository.insertRisk(risk)
}
