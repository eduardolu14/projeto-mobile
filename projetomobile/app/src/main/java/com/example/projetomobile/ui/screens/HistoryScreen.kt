package com.example.projetomobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.ui.theme.ProjetomobileTheme

/**
 * A tela que exibe o histórico de agendamentos passados do usuário.
 * @param navController O NavController da barra inferior.
 */
@Composable
fun HistoryScreen(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Histórico", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.7f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de dados de exemplo para o histórico, agora usando a estrutura correta de Appointment.
        val appointments = listOf(
            Appointment(title = "Dr. Carlos Andrade", date = "15 de fev, Quinta-feira", time = "11:00"),
            Appointment(title = "Dra. Beatriz Melo", date = "10 de jan, Segunda-feira", time = "09:30"),
        )

        // LazyColumn para exibir a lista de histórico de forma eficiente.
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(appointments) { appointment ->
                // Passa as funções de clique necessárias, mesmo que vazias por enquanto.
                AppointmentItem(
                    appointment = appointment,
                    onDeleteClick = { /* A fazer: Lógica de exclusão para o histórico */ },
                    onEditClick = { /* A fazer: Lógica de edição para o histórico */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    ProjetomobileTheme {
        HistoryScreen(navController = rememberNavController())
    }
}
