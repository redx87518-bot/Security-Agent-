package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_events")
data class LogEventEntity(
    @PrimaryKey val id: String,
    val source: String,
    val timestamp: Long,
    val eventType: String,
    val severity: String,
    val rawContent: String,
    val normalizedJson: String,
    val investigationId: String?
)
