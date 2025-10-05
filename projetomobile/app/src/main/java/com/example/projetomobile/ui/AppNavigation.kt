package com.example.projetomobile.ui

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projetomobile.ui.screens.Appointment
import com.example.projetomobile.ui.screens.MainScreen
import com.example.projetomobile.ui.screens.ReminderScreen
import com.example.projetomobile.ui.screens.WelcomeScreen
import java.util.UUID

/**
 * Gerencia a navegação principal e o estado compartilhado do aplicativo.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val appointments = remember { mutableStateListOf<Appointment>() }

    /**
     * Adiciona um novo agendamento e navega de volta.
     */
    val onAddAppointment: (Appointment) -> Unit = { newAppointment ->
        appointments.add(newAppointment)
        navController.popBackStack()
    }

    /**
     * Remove um agendamento da lista.
     */
    val onDeleteAppointment: (Appointment) -> Unit = { appointment ->
        appointments.remove(appointment)
    }

    /**
     * Atualiza um agendamento existente.
     */
    val onUpdateAppointment: (Appointment) -> Unit = { updatedAppointment ->
        val index = appointments.indexOfFirst { it.id == updatedAppointment.id }
        if (index != -1) {
            appointments[index] = updatedAppointment
        }
        navController.popBackStack()
    }

    /**
     * Navega para a tela de lembrete para edição.
     */
    val onEditAppointment: (Appointment) -> Unit = { appointment ->
        navController.navigate("reminder/${appointment.id}")
    }

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }

        composable("main") {
            MainScreen(
                navController = navController,
                appointments = appointments,
                onDeleteAppointment = onDeleteAppointment,
                onEditAppointment = onEditAppointment
            )
        }

        // Rota para criar um novo lembrete.
        composable("reminder") {
            ReminderScreen(
                navController = navController,
                onAddAppointment = onAddAppointment,
                onUpdateAppointment = onUpdateAppointment // Passa a função de atualização
            )
        }

        // Rota para editar um lembrete existente. Recebe o ID como argumento.
        composable(
            route = "reminder/{appointmentId}",
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
            val appointmentToEdit = appointments.firstOrNull { it.id.toString() == appointmentId }

            ReminderScreen(
                navController = navController,
                onAddAppointment = onAddAppointment,
                onUpdateAppointment = onUpdateAppointment,
                appointmentToEdit = appointmentToEdit
            )
        }
    }
}
