package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indicators")
data class IndicatorEntity(
    @PrimaryKey val id: String,
    val value: String,
    val type: String,
    val severity: String,
    val confidence: Int,
    val threatScore: Int,
    val sourceCount: Int,
    val createdAt: Long,
    val investigationId: String?
)
