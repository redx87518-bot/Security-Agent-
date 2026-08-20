package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_timeline_events")
data class IncidentTimelineEventEntity(
    @PrimaryKey val id: String,
    val incidentId: String,
    val timestamp: Long,
    val eventType: String,
    val description: String,
    val actor: String?,
    val evidenceIds: String
)
