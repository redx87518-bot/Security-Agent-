package com.cyberfusion.ai.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        when (uiState) {
            is SettingsUiState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Loading settings...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is SettingsUiState.Success -> {
                val success = uiState as SettingsUiState.Success
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("AI Provider Configuration", style = MaterialTheme.typography.titleSmall)
                        Text("Current Provider: ${success.configuredProvider}", style = MaterialTheme.typography.bodySmall)
                        Text("Status: ${if (success.isAvailable) "Available" else "Unavailable or not configured"}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is SettingsUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as SettingsUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Offline Behavior", style = MaterialTheme.typography.titleSmall)
                Text("Local data remains available offline.", style = MaterialTheme.typography.bodySmall)
                Text("AI features require connectivity.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Security", style = MaterialTheme.typography.titleSmall)
                Text("API keys stored in encrypted DataStore.", style = MaterialTheme.typography.bodySmall)
                Text("Audit logging enabled.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
