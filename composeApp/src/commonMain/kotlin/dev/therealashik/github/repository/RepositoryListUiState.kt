package dev.therealashik.github.repository

sealed class RepositoryListUiState {
    data object Loading : RepositoryListUiState()
    data class Error(val message: String) : RepositoryListUiState()
    data class Success(val repositories: List<RepositoryItem>) : RepositoryListUiState()
}

data class RepositoryItem(
    val id: String,
    val name: String,
    val description: String?,
    val forkedFrom: String?,
    val stars: Int,
    val language: String?
)
