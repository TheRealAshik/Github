package dev.therealashik.github.settings

import androidx.lifecycle.ViewModel
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {
    private val tokenStorage = createTokenStorage()

    private val _uiState = MutableStateFlow(buildState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun refresh() {
        _uiState.value = buildState()
    }

    private fun buildState(): SettingsUiState {
        val hasToken = tokenStorage.getToken() != null
        return SettingsUiState(accountsCount = if (hasToken) 1 else 0)
    }
}
