package dev.therealashik.github

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.therealashik.github.profile.ProfileScreen
import dev.therealashik.github.profile.ProfileViewModel
import dev.therealashik.github.profile.StarredScreen
import dev.therealashik.github.profile.StarredViewModel
import dev.therealashik.github.repository.RepositoryListScreen
import dev.therealashik.github.repository.RepositoryListViewModel
import dev.therealashik.github.settings.AddPatScreen
import dev.therealashik.github.settings.CodeOptionsScreen
import dev.therealashik.github.settings.NotificationOptionsScreen
import dev.therealashik.github.settings.SettingsScreen

private val LightColorScheme = lightColorScheme()
private val DarkColorScheme = darkColorScheme()

@Composable
fun App() {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

    MaterialTheme(colorScheme = colorScheme) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Route.Splash) {
            composable<Route.Splash> {
                SplashScreen(onSplashFinished = {
                    navController.navigate(Route.Main) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                })
            }
            composable<Route.Main> {
                MainScreen(
                    onNavigateToProfile = { navController.navigate(Route.Profile) }
                )
            }
            composable<Route.Profile> {
                ProfileScreen(
                    viewModel = ProfileViewModel(),
                    onBack = { navController.popBackStack() },
                    onNavigateToRepositories = { navController.navigate(Route.Repositories) },
                    onNavigateToStarred = { navController.navigate(Route.Starred) },
                    onNavigateToSettings = { navController.navigate(Route.Settings) }
                )
            }
            composable<Route.Starred> {
                StarredScreen(
                    viewModel = StarredViewModel(),
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Route.Repositories> {
                RepositoryListScreen(
                    viewModel = RepositoryListViewModel(),
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Route.Settings> {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToNotificationOptions = { navController.navigate(Route.NotificationOptions) },
                    onNavigateToCodeOptions = { navController.navigate(Route.CodeOptions) },
                    onNavigateToAddPat = { navController.navigate(Route.AddPat) },
                    onSignOut = {
                        navController.navigate(Route.AddPat) {
                            popUpTo(Route.Settings) { inclusive = true }
                        }
                    }
                )
            }
            composable<Route.NotificationOptions> {
                NotificationOptionsScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable<Route.CodeOptions> {
                CodeOptionsScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable<Route.AddPat> {
                AddPatScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }
        }
    }
}
