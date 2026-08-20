package com.cyberfusion.ai.feature.incident.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun IncidentScreen(navController: NavController, viewModel: IncidentViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Incident Management", style = MaterialTheme.typography.titleLarge)
        when (uiState) {
            is IncidentUiState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Loading incidents...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is IncidentUiState.Empty -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No incidents found.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is IncidentUiState.Success -> {
                val incidents = (uiState as IncidentUiState.Success).incidents
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(incidents) { incident ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("ID: ${incident.id}", style = MaterialTheme.typography.titleSmall)
                                Text("Title: ${incident.title}", style = MaterialTheme.typography.bodySmall)
                                Text("Status: ${incident.status}", style = MaterialTheme.typography.bodySmall)
                                Text("Severity: ${incident.severity}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            is IncidentUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as IncidentUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
