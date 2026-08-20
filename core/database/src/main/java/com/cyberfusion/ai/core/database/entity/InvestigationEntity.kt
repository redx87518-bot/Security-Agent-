package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investigations")
data class InvestigationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val status: String,
    val severity: String,
    val createdAt: Long,
    val updatedAt: Long,
    val owner: String?
)
