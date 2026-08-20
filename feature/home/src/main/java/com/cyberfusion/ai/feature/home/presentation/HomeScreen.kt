package com.cyberfusion.ai.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Security Posture",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        when (uiState) {
            is HomeUiState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Loading security posture...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is HomeUiState.Success -> {
                val success = uiState as HomeUiState.Success
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Open Incidents", style = MaterialTheme.typography.bodySmall)
                            Text("${success.openIncidents}", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Critical Findings", style = MaterialTheme.typography.bodySmall)
                            Text("${success.criticalFindings}", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
            is HomeUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as HomeUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { navController.navigate("intelligence") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Analyze IOC")
                }
                Button(onClick = { navController.navigate("loganalysis") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Analyze Logs")
                }
                Button(onClick = { navController.navigate("incident") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Create Incident")
                }
                Button(onClick = { navController.navigate("grc") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Assess Risk")
                }
                Button(onClick = { navController.navigate("reports") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Generate Report")
                }
            }
        }
        Button(
            onClick = { navController.navigate("reports") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AI Command Center")
        }
    }
}
