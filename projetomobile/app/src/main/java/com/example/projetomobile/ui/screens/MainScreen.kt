package com.example.projetomobile.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Um item da barra de navegação inferior.
 */
data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

/**
 * A tela principal do aplicativo, exibida após o login.
 */
@Composable
fun MainScreen(
    navController: NavController,
    appointments: List<Appointment>,
    onDeleteAppointment: (Appointment) -> Unit,
    onEditAppointment: (Appointment) -> Unit
) {
    val bottomBarNavController = rememberNavController()

    /**
     * Função de logout.
     */
    fun onLogout() {
        navController.navigate("welcome") {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val items = listOf(
                    BottomNavItem("home", Icons.Default.Home, "Home"),
                    BottomNavItem("history", Icons.Default.DateRange, "Histórico"),
                    BottomNavItem("profile", Icons.Default.Person, "Perfil")
                )

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            bottomBarNavController.navigate(item.route) {
                                popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("reminder") }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Lembrete")
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomBarNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    navController = bottomBarNavController,
                    appointments = appointments,
                    onDeleteAppointment = onDeleteAppointment,
                    onEditAppointment = onEditAppointment
                )
            }
            composable("history") { HistoryScreen(navController = bottomBarNavController) }
            composable("profile") { ProfileScreen(navController = bottomBarNavController, onLogoutClick = ::onLogout) }
        }
    }
}
