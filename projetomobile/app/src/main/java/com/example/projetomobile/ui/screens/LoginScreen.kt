package com.example.projetomobile.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.R
import com.example.projetomobile.ui.theme.ProjetomobileTheme
import kotlinx.coroutines.launch

/**
 * A tela inicial de boas-vindas do aplicativo.
 * Apresenta o logo e os botões para "Entrar" ou "Cadastre-se".
 * Gerencia um ModalBottomSheet (pop-up) para os formulários de autenticação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isLogin by remember { mutableStateOf(true) }

    // Função para navegar para a tela principal após o sucesso do login/cadastro.
    val onAuthSuccess: () -> Unit = {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
                navController.navigate("main") { popUpTo("welcome") { inclusive = true } }
            }
        }
    }

    // Conteúdo principal da tela de boas-vindas.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Health Care Logo",
            modifier = Modifier.height(120.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Botão para mostrar o formulário de login no pop-up.
        Button(
            onClick = {
                isLogin = true
                showBottomSheet = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botão para mostrar o formulário de cadastro no pop-up.
        OutlinedButton(
            onClick = {
                isLogin = false
                showBottomSheet = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cadastre-se")
        }
    }

    // Lógica para exibir o ModalBottomSheet.
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }, // O que fazer quando o usuário fecha o pop-up.
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.7f) // Ocupa 70% da altura da tela.
        ) {
            // Conteúdo do pop-up.
            AuthSheetContent(
                isLogin = isLogin,
                onToggle = { isLogin = !isLogin }, // Função para alternar entre login e cadastro.
                onAuthSuccess = onAuthSuccess
            )
        }
    }
}

/**
 * Contêiner para os formulários de autenticação dentro do pop-up.
 * Usa AnimatedContent para uma transição suave entre os formulários.
 */
@Composable
private fun AuthSheetContent(isLogin: Boolean, onToggle: () -> Unit, onAuthSuccess: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(targetState = isLogin, label = "auth_form") { isLoginTarget ->
            if (isLoginTarget) {
                LoginForm(onSignUpClick = onToggle, onLoginSuccess = onAuthSuccess)
            } else {
                SignUpForm(onLoginClick = onToggle, onSignUpSuccess = onAuthSuccess)
            }
        }
    }
}

/**
 * Formulário de Login com lógica de validação.
 */
@Composable
private fun LoginForm(onSignUpClick: () -> Unit, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Login", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = authTextFieldColors(),
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = authTextFieldColors(),
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val savedEmail = prefs.getString("email", null)
                val savedPassword = prefs.getString("password", null)

                if (email == savedEmail && password == savedPassword) {
                    onLoginSuccess()
                } else {
                    errorMessage = "Email ou senha inválidos"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Entrar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSignUpClick) {
            Text("Não tem uma conta? Cadastre-se", color = Color.White, textAlign = TextAlign.Center)
        }
    }
}

/**
 * Formulário de Cadastro com lógica para salvar os dados.
 */
@Composable
private fun SignUpForm(onLoginClick: () -> Unit, onSignUpSuccess: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cadastro", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; errorMessage = null },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            colors = authTextFieldColors(),
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = authTextFieldColors(),
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = authTextFieldColors(),
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    errorMessage = "Todos os campos são obrigatórios"
                } else {
                    // Salva as credenciais. NOTA: Em um app real, a senha deve ser criptografada (hashed).
                    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    prefs.edit().apply {
                        putString("email", email)
                        putString("password", password) // NÃO FAÇA ISSO EM PRODUÇÃO!
                        apply()
                    }
                    onSignUpSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Cadastrar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onLoginClick) {
            Text("Já possui cadastro? Entre aqui", color = Color.White, textAlign = TextAlign.Center)
        }
    }
}

/**
 * Uma função de conveniência para padronizar as cores dos OutlinedTextFields nos formulários.
 */
@Composable
private fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
    cursorColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    errorBorderColor = MaterialTheme.colorScheme.error,
    errorLabelColor = MaterialTheme.colorScheme.error
)


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ProjetomobileTheme {
        WelcomeScreen(navController = rememberNavController())
    }
}
