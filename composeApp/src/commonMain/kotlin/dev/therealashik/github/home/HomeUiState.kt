package dev.therealashik.github.home

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    data class Success(
        val repos: List<RepoItem>,
        val orgs: List<OrgItem>,
        val notifications: List<NotificationItem>,
        val starred: List<StarredItem>,
        val avatarUrl: String
    ) : HomeUiState()
}

data class RepoItem(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val isPrivate: Boolean,
    val updatedAt: String?
)

data class OrgItem(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val description: String?
)

data class NotificationItem(
    val id: String,
    val repoFullName: String,
    val title: String,
    val type: String,
    val isUnread: Boolean,
    val updatedAt: String
)


data class StarredItem(
    val id: Long,
    val name: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String
)