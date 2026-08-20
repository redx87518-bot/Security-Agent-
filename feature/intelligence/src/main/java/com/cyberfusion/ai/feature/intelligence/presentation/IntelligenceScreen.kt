package com.cyberfusion.ai.feature.intelligence.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun IntelligenceScreen(navController: NavController, viewModel: IntelligenceViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var query by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Threat Intelligence", style = MaterialTheme.typography.titleLarge)
        when (uiState) {
            is IntelligenceUiState.NotConfigured -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Intelligence provider not configured", style = MaterialTheme.typography.titleSmall)
                        Text("Configure an AI provider in Settings to enable threat intelligence analysis.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Enter IOC (IP, domain, hash)") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(onClick = { viewModel.analyzeIndicator(query) }, enabled = query.isNotBlank()) {
                    Text("Analyze Indicator")
                }
            }
        }
        when (uiState) {
            is IntelligenceUiState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Analyzing...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is IntelligenceUiState.Success -> {
                val success = uiState as IntelligenceUiState.Success
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Indicator: ${success.indicator}", style = MaterialTheme.typography.titleSmall)
                        Text("Type: ${success.type}", style = MaterialTheme.typography.bodySmall)
                        Text("Severity: ${success.severity}", style = MaterialTheme.typography.bodySmall)
                        Text("Confidence: ${success.confidence}%", style = MaterialTheme.typography.bodySmall)
                        Text("Threat Score: ${success.threatScore.score}", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            is IntelligenceUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as IntelligenceUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> Unit
        }
    }
}
