package com.cyberfusion.ai.feature.grc.presentation

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
fun GrcScreen(navController: NavController, viewModel: GrcViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("GRC & Risk Management", style = MaterialTheme.typography.titleLarge)
        when (uiState) {
            is GrcUiState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Loading risks...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is GrcUiState.Empty -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Risk Register is empty.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is GrcUiState.Success -> {
                val risks = (uiState as GrcUiState.Success).risks
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(risks) { risk ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("ID: ${risk.id}", style = MaterialTheme.typography.titleSmall)
                                Text("Title: ${risk.title}", style = MaterialTheme.typography.bodySmall)
                                Text("Score: ${risk.riskScore} (${risk.treatment})", style = MaterialTheme.typography.bodySmall)
                                Text("Status: ${risk.status}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            is GrcUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as GrcUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
