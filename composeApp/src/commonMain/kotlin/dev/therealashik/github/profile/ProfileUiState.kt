package dev.therealashik.github.profile

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
    data class Success(
        val login: String,
        val name: String?,
        val avatarUrl: String,
        val bio: String?,
        val company: String?,
        val location: String?,
        val followers: Int,
        val following: Int,
        val publicRepos: Int,
        val popularRepos: List<PopularRepo>,
        val orgs: List<OrgSummary>,
        val starredCount: Int
    ) : ProfileUiState()
}

data class PopularRepo(
    val id: Long,
    val owner: String,
    val name: String,
    val description: String?,
    val stars: Int,
    val language: String?
)

data class OrgSummary(
    val id: Long,
    val login: String,
    val avatarUrl: String
)
