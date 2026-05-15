package dev.therealashik.github.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CodeOptionsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CodeOptionsUiState())
    val uiState: StateFlow<CodeOptionsUiState> = _uiState.asStateFlow()

    fun toggleScrollableFilePath() {
        _uiState.update { it.copy(scrollableFilePath = !it.scrollableFilePath) }
    }

    fun toggleShowLineNumbers() {
        _uiState.update { it.copy(showLineNumbers = !it.showLineNumbers) }
    }

    fun toggleAlwaysUseDarkTheme() {
        _uiState.update { it.copy(alwaysUseDarkTheme = !it.alwaysUseDarkTheme) }
    }

    fun toggleOverrideSystemFontSize() {
        _uiState.update { it.copy(overrideSystemFontSize = !it.overrideSystemFontSize) }
    }

    fun toggleWrapLines() {
        _uiState.update { it.copy(wrapLines = !it.wrapLines) }
    }
}
