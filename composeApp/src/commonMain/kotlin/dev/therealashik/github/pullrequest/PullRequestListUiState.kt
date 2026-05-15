package dev.therealashik.github.pullrequest

data class PullRequestItem(
    val id: Long,
    val title: String,
    val number: Int,
    val timestamp: String,
    val author: String,
    val isUnread: Boolean,
    val checksSummary: String,
    val commentsCount: Int
)

data class PullRequestListUiState(
    val ownerName: String = "",
    val repoName: String = "",
    val pullRequests: List<PullRequestItem> = emptyList(),
    val activeFilter: String? = null
)
