package dev.therealashik.github.settings

import kotlinx.coroutines.flow.MutableStateFlow

enum class ThemePreference {
    SYSTEM,
    LIGHT,
    DARK
}

object ThemeStateHolder {
    val themePreference = MutableStateFlow(ThemePreference.SYSTEM)
}
