package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val severity: String,
    val status: String,
    val owner: String?,
    val affectedAssets: String,
    val createdAt: Long,
    val updatedAt: Long,
    val closedAt: Long?
)
