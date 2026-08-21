package com.cyberfusion.ai.feature.reports.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ReportsScreen(navController: NavController, viewModel: ReportsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Reports", style = MaterialTheme.typography.titleLarge)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.generateReport() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Generate Security Report")
                }
            }
        }
        when (uiState) {
            is ReportsUiState.Generating -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Generating report...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is ReportsUiState.Success -> {
                val success = uiState as ReportsUiState.Success
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Report Generated", style = MaterialTheme.typography.titleSmall)
                        Text("Saved to: ${success.path}", style = MaterialTheme.typography.bodySmall)
                        Text("Files created:", style = MaterialTheme.typography.bodySmall)
                        Text("- report_*.json", style = MaterialTheme.typography.bodySmall)
                        Text("- report_*.md", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is ReportsUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as ReportsUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> Unit
        }
    }
}
