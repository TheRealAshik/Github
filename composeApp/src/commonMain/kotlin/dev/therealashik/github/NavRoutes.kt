package dev.therealashik.github

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Main : Route
    @Serializable data object Profile : Route
    @Serializable data object Repositories : Route
    @Serializable data object Settings : Route
    @Serializable data object NotificationOptions : Route
    @Serializable data object CodeOptions : Route
    @Serializable data object AddPat : Route
    @Serializable data object Starred : Route
}
