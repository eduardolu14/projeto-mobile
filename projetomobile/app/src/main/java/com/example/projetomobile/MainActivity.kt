package com.example.projetomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.projetomobile.ui.AppNavigation
import com.example.projetomobile.ui.theme.ProjetomobileTheme

/**
 * A atividade principal (MainActivity) é o ponto de entrada para a interface do usuário do aplicativo.
 * Ela herda de ComponentActivity, que é a classe base para atividades que usam Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    /**
     * O método onCreate é chamado quando a atividade é criada pela primeira vez.
     * É aqui que a interface do usuário é definida.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent é a função principal do Compose. Ela define o conteúdo da atividade
        // com um Composable.
        setContent {
            // ProjetomobileTheme é o nosso tema customizado (cores, fontes, etc.).
            // Envolvemos todo o aplicativo nele para garantir uma aparência consistente.
            ProjetomobileTheme {
                // Surface é um contêiner do Material Design que aplica uma cor de fundo.
                Surface(
                    modifier = Modifier.fillMaxSize(), // Ocupa toda a tela.
                    color = MaterialTheme.colorScheme.background // Usa a cor de fundo do tema.
                ) {
                    // AppNavigation é o Composable que gerencia toda a navegação do aplicativo.
                    AppNavigation()
                }
            }
        }
    }
}
