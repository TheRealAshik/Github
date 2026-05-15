package dev.therealashik.github

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private val LightColorScheme = lightColorScheme()
private val DarkColorScheme = darkColorScheme()

@Composable
fun App() {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

    MaterialTheme(colorScheme = colorScheme) {
        var currentScreen by remember { mutableStateOf("splash") }

        Crossfade(targetState = currentScreen) { screen ->
            when (screen) {
                "splash" -> SplashScreen(onSplashFinished = { currentScreen = "main" })
                "main" -> MainScreen(
                    onNavigateToProfile = { currentScreen = "profile" }
                )
                "profile" -> dev.therealashik.github.profile.ProfileScreen(
                    viewModel = dev.therealashik.github.profile.ProfileViewModel(),
                    onBack = { currentScreen = "main" },
                    onNavigateToRepositories = { currentScreen = "repositories" }
                )
                "repositories" -> dev.therealashik.github.repository.RepositoryListScreen(
                    viewModel = dev.therealashik.github.repository.RepositoryListViewModel(),
                    onBack = { currentScreen = "profile" }
                )
            }
        }
    }
}
