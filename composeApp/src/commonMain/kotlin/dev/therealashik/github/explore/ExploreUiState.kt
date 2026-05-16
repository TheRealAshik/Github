package dev.therealashik.github.explore

sealed class ActivityItem {
    abstract val id: String
}

data class ContributionItem(
    override val id: String,
    val username: String,
    val actionText: String,
    val timestamp: String,
    val repoPath: String,
    val prTitle: String,
    val statusText: String,
    val branchName: String,
    val bodyPreview: String
) : ActivityItem()

data class ReleaseItem(
    override val id: String,
    val botName: String,
    val actionText: String,
    val timestamp: String,
    val releaseTitle: String
) : ActivityItem()

data class TrendingRepoItem(
    val id: String,
    val fullName: String,
    val description: String?,
    val stars: Int,
    val language: String?
)

sealed class ExploreUiState {
    data object Loading : ExploreUiState()
    data class Error(val message: String) : ExploreUiState()
    data class Success(
        val trendingRepos: List<TrendingRepoItem> = emptyList(),
        val activityFeed: List<ActivityItem> = emptyList()
    ) : ExploreUiState()
}
