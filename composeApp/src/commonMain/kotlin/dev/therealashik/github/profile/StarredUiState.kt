package dev.therealashik.github.profile

sealed class StarredUiState {
    data object Loading : StarredUiState()
    data class Error(val message: String) : StarredUiState()
    data class Success(val starredRepos: List<StarredRepoItem>) : StarredUiState()
}

data class StarredRepoItem(
    val id: String,
    val name: String,
    val description: String?,
    val stars: Int,
    val language: String?
)
