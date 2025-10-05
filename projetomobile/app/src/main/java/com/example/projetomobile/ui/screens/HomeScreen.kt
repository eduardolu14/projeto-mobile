package com.example.projetomobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.ui.theme.ProjetomobileTheme
import java.util.UUID

/**
 * Representa os dados de um único Lembrete/Agendamento.
 * Adicionamos um id para identificar unicamente cada item.
 */
data class Appointment(val id: UUID = UUID.randomUUID(), val title: String, val date: String, val time: String)

/**
 * A tela inicial que o usuário vê após o login.
 * @param appointments A lista de agendamentos a ser exibida.
 * @param onDeleteAppointment Callback para quando o botão de excluir é clicado.
 * @param onEditAppointment Callback para quando o botão de editar é clicado.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    appointments: List<Appointment>,
    onDeleteAppointment: (Appointment) -> Unit,
    onEditAppointment: (Appointment) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Futuras", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth(),
            colors = homeScreenTextFieldColors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(appointments) { appointment ->
                AppointmentItem(
                    appointment = appointment,
                    onDeleteClick = { onDeleteAppointment(appointment) },
                    onEditClick = { onEditAppointment(appointment) }
                )
            }
        }
    }
}

/**
 * O Composable para um único item na lista de agendamentos.
 * Agora inclui botões de editar e excluir.
 */
@Composable
fun AppointmentItem(
    appointment: Appointment,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = appointment.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Data: ${appointment.date}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Hora: ${appointment.time}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir")
            }
        }
    }
}

@Composable
private fun homeScreenTextFieldColors() = OutlinedTextFieldDefaults.colors(
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProjetomobileTheme {
        val sampleAppointments = listOf(
            Appointment(title = "Cardiologista", date = "28 de fev, Sábado", time = "10:00"),
            Appointment(title = "Dermatologista", date = "02 de mar, Segunda-feira", time = "14:30"),
        )
        HomeScreen(
            navController = rememberNavController(),
            appointments = sampleAppointments,
            onDeleteAppointment = {},
            onEditAppointment = {}
        )
    }
}
