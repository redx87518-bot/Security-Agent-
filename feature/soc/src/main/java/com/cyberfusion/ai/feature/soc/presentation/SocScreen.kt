package com.cyberfusion.ai.feature.soc.presentation

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SocScreen(navController: NavController, viewModel: SocViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val alerts by viewModel.alerts.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("SOC Dashboard", style = MaterialTheme.typography.titleLarge)
        when (uiState) {
            is SocUiState.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(alerts) { alert ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(alert.title, style = MaterialTheme.typography.titleMedium)
                                Text(alert.severity, style = MaterialTheme.typography.bodySmall)
                                Text(alert.description, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    if (alerts.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("No alerts yet.", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
            is SocUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as SocUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Loading alerts...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
