package com.cyberfusion.ai.feature.loganalysis.presentation

import android.content.Context
import android.content.Intent
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun LogAnalysisScreen(navController: NavController, viewModel: LogAnalysisViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var logContent by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            viewModel.setSelectedFileUri(uri.toString())
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()
                logContent = content
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Log Analysis", style = MaterialTheme.typography.titleLarge)
        Button(onClick = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, android.provider.MediaStore.Files.getContentUri("external"))
            }
            filePickerLauncher.launch(intent)
        }) {
            Text("Import Log File")
        }
        viewModel.selectedFileUri?.let { uri ->
            Text("Selected: $uri", style = MaterialTheme.typography.bodySmall)
        }
        OutlinedTextField(
            value = logContent,
            onValueChange = { logContent = it },
            label = { Text("Paste or type log content") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 6,
            maxLines = 12
        )
        Button(onClick = { viewModel.analyzeLogs(logContent) }, enabled = logContent.isNotBlank()) {
            Text("Analyze Logs")
        }
        when (uiState) {
            is LogAnalysisUiState.Completed -> {
                val success = uiState as LogAnalysisUiState.Completed
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Events: ${success.eventCount}", style = MaterialTheme.typography.titleSmall)
                        Text("Summary: ${success.summary}", style = MaterialTheme.typography.bodySmall)
                        if (success.notablePatterns.isNotEmpty()) {
                            Text("Notable Patterns:", style = MaterialTheme.typography.titleSmall)
                            success.notablePatterns.forEach { pattern ->
                                Text("- ${pattern.patternType}: ${pattern.description}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            is LogAnalysisUiState.PartialResult -> {
                val partial = uiState as LogAnalysisUiState.PartialResult
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Partial Result", style = MaterialTheme.typography.titleSmall)
                        Text("Events: ${partial.eventCount}", style = MaterialTheme.typography.bodySmall)
                        Text("Summary: ${partial.summary}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is LogAnalysisUiState.Failed -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Analysis Failed: ${(uiState as LogAnalysisUiState.Failed).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is LogAnalysisUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error: ${(uiState as LogAnalysisUiState.Error).message}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            is LogAnalysisUiState.Offline -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Offline - Local analysis only", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            else -> Unit
        }
    }
}
