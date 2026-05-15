package dev.therealashik.github.settings

data class CodeOptionsUiState(
    val scrollableFilePath: Boolean = true,
    val showLineNumbers: Boolean = true,
    val alwaysUseDarkTheme: Boolean = false,
    val overrideSystemFontSize: Boolean = false,
    val wrapLines: Boolean = false
)
