package dev.therealashik.github.settings

data class SettingsUiState(
    val themeValue: String = "Follow system",
    val languageValue: String = "Follow system",
    val accounts: List<AccountItem> = emptyList(),
    val copilotTier: String = "Copilot Free",
    val appVersion: String = "GitHub Mobile v1.258.0-beta (10323)"
) {
    val accountsCount: Int get() = accounts.size
}
