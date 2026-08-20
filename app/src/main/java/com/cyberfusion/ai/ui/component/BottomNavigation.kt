package com.cyberfusion.ai.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cyberfusion.ai.ui.navigation.Screen

@Composable
fun CyberFusionBottomNavigation(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Soc,
        Screen.Intelligence,
        Screen.Grc,
        Screen.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val icon = when (screen) {
                        Screen.Home -> Icons.Home
                        Screen.Soc -> Icons.Soc
                        Screen.Intelligence -> Icons.Intelligence
                        Screen.Grc -> Icons.Incident
                        Screen.Settings -> Icons.Settings
                    }
                    Icon(icon, contentDescription = screen.title)
                },
                label = { Text(screen.title) }
            )
        }
    }
}
