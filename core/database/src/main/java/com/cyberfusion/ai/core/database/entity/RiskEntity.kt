package com.cyberfusion.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "risks")
data class RiskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val asset: String,
    val threat: String,
    val vulnerability: String,
    val likelihood: Int,
    val impact: Int,
    val riskScore: Int,
    val treatment: String,
    val residualLikelihood: Int?,
    val residualImpact: Int?,
    val residualRiskScore: Int?,
    val owner: String?,
    val targetDate: Long?,
    val status: String
)
