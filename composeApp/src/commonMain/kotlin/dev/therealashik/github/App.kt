package dev.therealashik.github

import androidx.compose.animation.Crossfade
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            surface = Color.Black,
            background = Color.Black
        )
    ) {
        var currentScreen by remember { mutableStateOf("splash") }

        Crossfade(targetState = currentScreen) { screen ->
            when (screen) {
                "splash" -> SplashScreen(onSplashFinished = { currentScreen = "main" })
                "main" -> MainScreen()
            }
        }
    }
}
