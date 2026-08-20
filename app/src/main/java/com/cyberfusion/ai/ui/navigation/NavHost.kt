package com.cyberfusion.ai.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyberfusion.ai.feature.grc.presentation.GrcScreen
import com.cyberfusion.ai.feature.home.presentation.AICommandCenterScreen
import com.cyberfusion.ai.feature.home.presentation.HomeScreen
import com.cyberfusion.ai.feature.incident.presentation.IncidentScreen
import com.cyberfusion.ai.feature.intelligence.presentation.IntelligenceScreen
import com.cyberfusion.ai.feature.loganalysis.presentation.LogAnalysisScreen
import com.cyberfusion.ai.feature.reports.presentation.ReportsScreen
import com.cyberfusion.ai.feature.settings.presentation.SettingsScreen
import com.cyberfusion.ai.feature.soc.presentation.SocScreen
import com.cyberfusion.ai.ui.component.CyberFusionBottomNavigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
    data object Soc : Screen("soc", "SOC")
    data object Intelligence : Screen("intelligence", "Intelligence")
    data object Incident : Screen("incident", "Incident")
    data object Grc : Screen("grc", "GRC")
    data object Reports : Screen("reports", "Reports")
    data object LogAnalysis : Screen("loganalysis", "Logs")
    data object AICommandCenter : Screen("aicommandcenter", "AI Command")
    data object Settings : Screen("settings", "More")
}

@Composable
fun CyberFusionNavHost(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { CyberFusionBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Soc.route) { SocScreen(navController) }
            composable(Screen.Intelligence.route) { IntelligenceScreen(navController) }
            composable(Screen.Incident.route) { IncidentScreen(navController) }
            composable(Screen.Grc.route) { GrcScreen(navController) }
            composable(Screen.Reports.route) { ReportsScreen(navController) }
            composable(Screen.LogAnalysis.route) { LogAnalysisScreen(navController) }
            composable(Screen.AICommandCenter.route) { AICommandCenterScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
        }
    }
}
