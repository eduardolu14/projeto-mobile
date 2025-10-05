package com.example.projetomobile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.ui.theme.ProjetomobileTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Tela para adicionar ou editar um novo lembrete.
 * @param onAddAppointment Callback para adicionar um novo agendamento.
 * @param onUpdateAppointment Callback para atualizar um agendamento existente.
 * @param appointmentToEdit O agendamento a ser editado (se houver), ou null se for um novo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onAddAppointment: (Appointment) -> Unit = {},
    onUpdateAppointment: (Appointment) -> Unit = {},
    appointmentToEdit: Appointment? = null
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var time by remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }

    // Determina se estamos em modo de edição.
    val isEditing = appointmentToEdit != null

    // Preenche os campos se estivermos editando um agendamento.
    // LaunchedEffect garante que isso rode apenas uma vez quando a tela é aberta para edição.
    LaunchedEffect(appointmentToEdit) {
        if (isEditing) {
            title = appointmentToEdit!!.title
            time = appointmentToEdit.time
            // Converte a data de String (dd/MM/yyyy) de volta para Long (timestamp).
            try {
                selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(appointmentToEdit.date)?.time
            } catch (e: Exception) {
                selectedDate = null // Lida com possíveis erros de formatação.
            }
        }
    }

    val formattedDate = remember(selectedDate) {
        selectedDate?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
        } ?: ""
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = if (isEditing) "Editar Lembrete" else "Adicionar Lembrete", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = formattedDate,
                onValueChange = { },
                label = { Text("Data") },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker.value = true },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Hora") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && selectedDate != null && time.isNotBlank()) {
                    if (isEditing) {
                        // Atualiza o agendamento existente mantendo o mesmo ID.
                        val updatedAppointment = appointmentToEdit!!.copy(
                            title = title,
                            date = formattedDate,
                            time = time
                        )
                        onUpdateAppointment(updatedAppointment)
                    } else {
                        // Cria um novo agendamento.
                        val newAppointment = Appointment(
                            title = title,
                            date = formattedDate,
                            time = time
                        )
                        onAddAppointment(newAppointment)
                    }
                } else {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Salvar lembrete")
        }
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis
                    showDatePicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderScreenPreview() {
    ProjetomobileTheme {
        ReminderScreen(navController = rememberNavController())
    }
}
