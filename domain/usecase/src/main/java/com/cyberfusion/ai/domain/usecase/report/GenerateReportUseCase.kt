package com.cyberfusion.ai.domain.usecase.report

import android.content.Context
import com.cyberfusion.ai.core.database.entity.AlertEntity
import com.cyberfusion.ai.core.database.entity.IncidentEntity
import com.cyberfusion.ai.core.database.entity.InvestigationEntity
import com.cyberfusion.ai.core.database.entity.RiskEntity
import com.cyberfusion.ai.core.database.repository.AlertRepository
import com.cyberfusion.ai.core.database.repository.IncidentRepository
import com.cyberfusion.ai.core.database.repository.InvestigationRepository
import com.cyberfusion.ai.core.database.repository.RiskRepository
import com.cyberfusion.ai.core.model.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GenerateReportUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val investigationRepository: InvestigationRepository,
    private val incidentRepository: IncidentRepository,
    private val alertRepository: AlertRepository,
    private val riskRepository: RiskRepository
) {
    suspend operator fun invoke(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val investigations = investigationRepository.getAllInvestigations().first()
            val incidents = incidentRepository.getAllIncidents().first()
            val alerts = alertRepository.getAllAlerts().first()
            val risks = riskRepository.getAllRisks().first()

            val invList = (investigations as? Result.Success)?.data ?: emptyList()
            val incList = (incidents as? Result.Success)?.data ?: emptyList()
            val alertList = (alerts as? Result.Success)?.data ?: emptyList()
            val riskList = (risks as? Result.Success)?.data ?: emptyList()

            val reportsDir = File(context.getExternalFilesDir(null), "reports")
            if (!reportsDir.exists()) {
                reportsDir.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val jsonFile = File(reportsDir, "report_$timestamp.json")
            val mdFile = File(reportsDir, "report_$timestamp.md")

            val reportData = JSONObject().apply {
                put("generatedAt", timestamp)
                put("reportType", "security_summary")
                put("summary", JSONObject().apply {
                    put("investigationCount", invList.size)
                    put("incidentCount", incList.size)
                    put("alertCount", alertList.size)
                    put("riskCount", riskList.size)
                })
                put("details", JSONObject().apply {
                    put("investigations", JSONArray(invList.map { inv ->
                        JSONObject().apply {
                            put("id", inv.id)
                            put("title", inv.title)
                            put("status", inv.status)
                            put("createdAt", inv.createdAt)
                        }
                    }))
                    put("incidents", JSONArray(incList.map { inc ->
                        JSONObject().apply {
                            put("id", inc.id)
                            put("title", inc.title)
                            put("severity", inc.severity)
                            put("status", inc.status)
                            put("createdAt", inc.createdAt)
                        }
                    }))
                    put("alerts", JSONArray(alertList.map { alert ->
                        JSONObject().apply {
                            put("id", alert.id)
                            put("title", alert.title)
                            put("severity", alert.severity)
                            put("status", alert.status)
                            put("source", alert.source)
                            put("createdAt", alert.createdAt)
                        }
                    }))
                    put("risks", JSONArray(riskList.map { risk ->
                        JSONObject().apply {
                            put("id", risk.id)
                            put("title", risk.title)
                            put("riskScore", risk.riskScore)
                            put("treatment", risk.treatment)
                            put("status", risk.status)
                        }
                    }))
                })
            }

            jsonFile.writeText(reportData.toString(2))

            val markdown = buildString {
                appendLine("# Security Report")
                appendLine("Generated: $timestamp")
                appendLine()
                appendLine("## Summary")
                appendLine("- Investigations: ${invList.size}")
                appendLine("- Incidents: ${incList.size}")
                appendLine("- Alerts: ${alertList.size}")
                appendLine("- Risks: ${riskList.size}")
                appendLine()
                if (incList.isNotEmpty()) {
                    appendLine("## Incidents")
                    incList.forEach { inc ->
                        appendLine("- ${inc.title} (${inc.severity})")
                    }
                    appendLine()
                }
                if (alertList.isNotEmpty()) {
                    appendLine("## Alerts")
                    alertList.forEach { alert ->
                        appendLine("- ${alert.title} (${alert.severity})")
                    }
                    appendLine()
                }
                if (riskList.isNotEmpty()) {
                    appendLine("## Risks")
                    riskList.forEach { risk ->
                        appendLine("- ${risk.title} (Score: ${risk.riskScore})")
                    }
                    appendLine()
                }
                appendLine("## Limitations")
                appendLine("This report is generated from locally stored data. Remote enrichment may not be available if offline.")
            }
            mdFile.writeText(markdown)

            Result.Success("Reports saved to: ${reportsDir.absolutePath}")
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Report generation failed")
        }
    }
}
