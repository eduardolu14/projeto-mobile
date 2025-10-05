package com.example.projetomobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.ui.theme.ProjetomobileTheme

/**
 * A tela de perfil do usuário.
 * Exibe informações básicas e fornece opções como "Compartilhar", "Editar" e "Sair".
 * @param navController O NavController da barra inferior.
 * @param onLogoutClick Uma função (callback) que é chamada quando o botão "Sair" é clicado.
 *                      Isso delega a lógica de logout para o Composable pai (MainScreen).
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Avatar de exemplo para o perfil do usuário.
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "MS", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Seu perfil", style = MaterialTheme.typography.titleLarge, color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: Implementar lógica de compartilhamento */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Compartilhar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextButton(onClick = { /* TODO: Implementar lógica de edição */ }) {
            Text(text = "Editar informações", color = Color.White)
        }

        // Botão que chama a função de logout fornecida pelo pai.
        TextButton(onClick = onLogoutClick) {
            Text(text = "Sair", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProjetomobileTheme {
        ProfileScreen(navController = rememberNavController(), onLogoutClick = {})
    }
}
