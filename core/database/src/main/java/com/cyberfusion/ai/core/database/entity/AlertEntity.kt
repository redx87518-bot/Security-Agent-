package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val severity: String,
    val status: String,
    val source: String,
    val createdAt: Long,
    val updatedAt: Long,
    val assetId: String?,
    val investigationId: String?
)
