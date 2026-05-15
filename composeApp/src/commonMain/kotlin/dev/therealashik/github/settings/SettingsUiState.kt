package dev.therealashik.github.settings

data class SettingsUiState(
    val themeValue: String = "Follow system",
    val languageValue: String = "Follow system",
    val accountsCount: Int = 2,
    val copilotTier: String = "Copilot Free",
    val appVersion: String = "GitHub Mobile v1.258.0-beta (10323)"
)
