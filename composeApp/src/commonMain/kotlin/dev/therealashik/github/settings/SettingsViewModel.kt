package dev.therealashik.github.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val tokenStorage = createTokenStorage()
    private val apiClient = GitHubApiClient(tokenStorage)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val token = tokenStorage.getToken()
            val accounts = if (token != null) {
                try {
                    val user = apiClient.getAuthenticatedUser().getOrThrow()
                    listOf(AccountItem(username = user.login, notificationCount = 0, isActive = true))
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
            _uiState.value = _uiState.value.copy(accounts = accounts)
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
