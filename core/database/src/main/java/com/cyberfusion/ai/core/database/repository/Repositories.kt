package com.cyberfusion.ai.core.database.repository

import com.cyberfusion.ai.core.database.dao.CyberFusionDao
import com.cyberfusion.ai.core.database.entity.AlertEntity
import com.cyberfusion.ai.core.database.entity.IncidentEntity
import com.cyberfusion.ai.core.database.entity.IndicatorEntity
import com.cyberfusion.ai.core.database.entity.InvestigationEntity
import com.cyberfusion.ai.core.database.entity.LogEventEntity
import com.cyberfusion.ai.core.database.entity.RiskEntity
import com.cyberfusion.ai.core.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestigationRepository @Inject constructor(
    private val dao: CyberFusionDao
) {
    fun getAllInvestigations(): Flow<Result<List<InvestigationEntity>>> =
        dao.getAllInvestigations().map { Result.Success(it) }
            .catch { emit(Result.Error(it, it.message)) }

    suspend fun getInvestigationById(id: String): Result<InvestigationEntity?> =
        try {
            Result.Success(dao.getInvestigationById(id))
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun insertInvestigation(investigation: InvestigationEntity): Result<Unit> =
        try {
            dao.insertInvestigation(investigation)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun updateInvestigation(investigation: InvestigationEntity): Result<Unit> =
        try {
            dao.updateInvestigation(investigation)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun deleteInvestigation(investigation: InvestigationEntity): Result<Unit> =
        try {
            dao.deleteInvestigation(investigation)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
}

@Singleton
class IndicatorRepository @Inject constructor(
    private val dao: CyberFusionDao
) {
    fun getAllIndicators(): Flow<Result<List<IndicatorEntity>>> =
        dao.getAllIndicators().map { Result.Success(it) }
            .catch { emit(Result.Error(it, it.message)) }

    suspend fun insertIndicator(indicator: IndicatorEntity): Result<Unit> =
        try {
            dao.insertIndicator(indicator)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
}

@Singleton
class IncidentRepository @Inject constructor(
    private val dao: CyberFusionDao
) {
    fun getAllIncidents(): Flow<Result<List<IncidentEntity>>> =
        dao.getAllIncidents().map { Result.Success(it) }
            .catch { emit(Result.Error(it, it.message)) }

    suspend fun getIncidentById(id: String): Result<IncidentEntity?> =
        try {
            Result.Success(dao.getIncidentById(id))
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun insertIncident(incident: IncidentEntity): Result<Unit> =
        try {
            dao.insertIncident(incident)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun updateIncident(incident: IncidentEntity): Result<Unit> =
        try {
            dao.updateIncident(incident)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
}

@Singleton
class RiskRepository @Inject constructor(
    private val dao: CyberFusionDao
) {
    fun getAllRisks(): Flow<Result<List<RiskEntity>>> =
        dao.getAllRisks().map { Result.Success(it) }
            .catch { emit(Result.Error(it, it.message)) }

    suspend fun getRiskById(id: String): Result<RiskEntity?> =
        try {
            Result.Success(dao.getRiskById(id))
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun insertRisk(risk: RiskEntity): Result<Unit> =
        try {
            dao.insertRisk(risk)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }

    suspend fun updateRisk(risk: RiskEntity): Result<Unit> =
        try {
            dao.updateRisk(risk)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
}

@Singleton
class AlertRepository @Inject constructor(
    private val dao: CyberFusionDao
) {
    fun getAllAlerts(): Flow<Result<List<AlertEntity>>> =
        dao.getAllAlerts().map { Result.Success(it) }
            .catch { emit(Result.Error(it, it.message)) }

    suspend fun insertAlert(alert: AlertEntity): Result<Unit> =
        try {
            dao.insertAlert(alert)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
}

@Singleton
class LogEventRepository @Inject constructor(
    private val dao: CyberFusionDao
) {
    fun getAllLogEvents(): Flow<Result<List<LogEventEntity>>> =
        dao.getAllLogEvents().map { Result.Success(it) }
            .catch { emit(Result.Error(it, it.message)) }

    suspend fun insertLogEvent(event: LogEventEntity): Result<Unit> =
        try {
            dao.insertLogEvent(event)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
}
