package dev.therealashik.github.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrganizationsViewModel : ViewModel() {
    private val apiClient = GitHubApiClient(createTokenStorage())
    private val _uiState = MutableStateFlow<OrganizationsUiState>(OrganizationsUiState.Loading)
    val uiState: StateFlow<OrganizationsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = OrganizationsUiState.Loading
            apiClient.getUserOrgs()
                .onSuccess { orgs ->
                    _uiState.value = OrganizationsUiState.Success(
                        orgs.map { o ->
                            OrgSummary(
                                id = o.id,
                                login = o.login,
                                avatarUrl = o.avatarUrl,
                                description = o.description
                            )
                        }
                    )
                }
                .onFailure { error ->
                    _uiState.value = OrganizationsUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
