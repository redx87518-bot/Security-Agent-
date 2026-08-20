package com.cyberfusion.ai.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cyberfusion.ai.core.database.entity.AlertEntity
import com.cyberfusion.ai.core.database.entity.IncidentEntity
import com.cyberfusion.ai.core.database.entity.IncidentTimelineEventEntity
import com.cyberfusion.ai.core.database.entity.IndicatorEntity
import com.cyberfusion.ai.core.database.entity.InvestigationEntity
import com.cyberfusion.ai.core.database.entity.LogEventEntity
import com.cyberfusion.ai.core.database.entity.RiskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CyberFusionDao {
    @Query("SELECT * FROM investigations ORDER BY updatedAt DESC")
    fun getAllInvestigations(): Flow<List<InvestigationEntity>>

    @Query("SELECT * FROM investigations WHERE id = :id")
    suspend fun getInvestigationById(id: String): InvestigationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestigation(investigation: InvestigationEntity)

    @Update
    suspend fun updateInvestigation(investigation: InvestigationEntity)

    @Delete
    suspend fun deleteInvestigation(investigation: InvestigationEntity)

    @Query("SELECT * FROM indicators ORDER BY createdAt DESC")
    fun getAllIndicators(): Flow<List<IndicatorEntity>>

    @Query("SELECT * FROM indicators WHERE investigationId = :investigationId")
    fun getIndicatorsByInvestigation(investigationId: String): Flow<List<IndicatorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndicator(indicator: IndicatorEntity)

    @Query("SELECT * FROM alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("SELECT * FROM incidents ORDER BY createdAt DESC")
    fun getAllIncidents(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM incidents WHERE id = :id")
    suspend fun getIncidentById(id: String): IncidentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity)

    @Update
    suspend fun updateIncident(incident: IncidentEntity)

    @Query("SELECT * FROM risks ORDER BY riskScore DESC")
    fun getAllRisks(): Flow<List<RiskEntity>>

    @Query("SELECT * FROM risks WHERE id = :id")
    suspend fun getRiskById(id: String): RiskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRisk(risk: RiskEntity)

    @Update
    suspend fun updateRisk(risk: RiskEntity)

    @Query("SELECT * FROM incident_timeline_events WHERE incidentId = :incidentId ORDER BY timestamp ASC")
    fun getTimelineEventsByIncident(incidentId: String): Flow<List<IncidentTimelineEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvent(event: IncidentTimelineEventEntity)

    @Query("SELECT * FROM log_events ORDER BY timestamp DESC")
    fun getAllLogEvents(): Flow<List<LogEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogEvent(event: LogEventEntity)
}
