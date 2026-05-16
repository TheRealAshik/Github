package dev.therealashik.github.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StarredViewModel : ViewModel() {
    private val apiClient = GitHubApiClient(createTokenStorage())
    private val _uiState = MutableStateFlow<StarredUiState>(StarredUiState.Loading)
    val uiState: StateFlow<StarredUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = StarredUiState.Loading
            apiClient.getStarredRepos(perPage = 100)
                .onSuccess { repos ->
                    _uiState.value = StarredUiState.Success(
                        repos.map { r ->
                            StarredRepoItem(
                                id = r.id.toString(),
                                name = r.name,
                                description = r.description,
                                stars = r.stars,
                                language = r.language
                            )
                        }
                    )
                }
                .onFailure { _uiState.value = StarredUiState.Error(it.message ?: "Unknown error") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
