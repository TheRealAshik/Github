package dev.therealashik.github.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoryListViewModel : ViewModel() {
    private val apiClient = GitHubApiClient(createTokenStorage())
    private val _uiState = MutableStateFlow<RepositoryListUiState>(RepositoryListUiState.Loading)
    val uiState: StateFlow<RepositoryListUiState> = _uiState.asStateFlow()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = RepositoryListUiState.Loading
            apiClient.getUserRepos(perPage = 100)
                .onSuccess { repos ->
                    _uiState.value = RepositoryListUiState.Success(
                        repos.map { r ->
                            RepositoryItem(
                                id = r.id.toString(),
                                name = r.name,
                                description = r.description,
                                forkedFrom = null,
                                stars = r.stars,
                                language = r.language
                            )
                        }
                    )
                }
                .onFailure { _uiState.value = RepositoryListUiState.Error(it.message ?: "Unknown error") }
        }
    }

    override fun onCleared() { apiClient.close() }
}
