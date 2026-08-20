package com.cyberfusion.ai.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
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
fun AICommandCenterScreen(navController: NavController, viewModel: AIAssistantViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var query by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("AI Command Center", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Ask the AI assistant") },
            modifier = Modifier.fillMaxWidth()
        )
        TextButton(onClick = { viewModel.sendQuery(query) }, enabled = query.isNotBlank()) {
            Text("Send")
        }
        when (uiState) {
            is AIAssistantUiState.Success -> {
                val success = uiState as AIAssistantUiState.Success
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Response:", style = MaterialTheme.typography.titleSmall)
                        Text(success.response, style = MaterialTheme.typography.bodySmall)
                        Text("Confidence: ${success.confidence}%", style = MaterialTheme.typography.bodySmall)
                        Text("Limitations: ${success.limitations.joinToString()}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is AIAssistantUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as AIAssistantUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> Unit
        }
    }
}
