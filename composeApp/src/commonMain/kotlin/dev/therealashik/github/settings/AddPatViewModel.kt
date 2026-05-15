package dev.therealashik.github.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddPatUiState(
    val token: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class AddPatViewModel : ViewModel() {
    private val tokenStorage = createTokenStorage()
    private val apiClient = GitHubApiClient(tokenStorage)

    private val _uiState = MutableStateFlow(AddPatUiState())
    val uiState: StateFlow<AddPatUiState> = _uiState.asStateFlow()

    fun onTokenChange(value: String) {
        _uiState.value = _uiState.value.copy(token = value, error = null)
    }

    fun saveToken() {
        val token = _uiState.value.token.trim()
        if (token.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Token cannot be empty")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            tokenStorage.saveToken(token)
            apiClient.getAuthenticatedUser().fold(
                onSuccess = { _uiState.value = _uiState.value.copy(isLoading = false, success = true) },
                onFailure = {
                    tokenStorage.clearToken()
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Invalid token or network error")
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
