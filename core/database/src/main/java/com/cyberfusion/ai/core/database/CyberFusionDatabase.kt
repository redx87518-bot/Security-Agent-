package com.cyberfusion.ai.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cyberfusion.ai.core.database.converter.Converters
import com.cyberfusion.ai.core.database.dao.CyberFusionDao
import com.cyberfusion.ai.core.database.entity.AlertEntity
import com.cyberfusion.ai.core.database.entity.IncidentEntity
import com.cyberfusion.ai.core.database.entity.IncidentTimelineEventEntity
import com.cyberfusion.ai.core.database.entity.IndicatorEntity
import com.cyberfusion.ai.core.database.entity.InvestigationEntity
import com.cyberfusion.ai.core.database.entity.LogEventEntity
import com.cyberfusion.ai.core.database.entity.RiskEntity

@Database(
    entities = [
        InvestigationEntity::class,
        IndicatorEntity::class,
        AlertEntity::class,
        IncidentEntity::class,
        RiskEntity::class,
        IncidentTimelineEventEntity::class,
        LogEventEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CyberFusionDatabase : RoomDatabase() {
    abstract fun cyberFusionDao(): CyberFusionDao
}
