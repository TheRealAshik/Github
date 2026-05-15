package dev.therealashik.github.explore

import org.jetbrains.compose.resources.StringResource

sealed class ActivityItem {
    abstract val id: String
}

data class ContributionItem(
    override val id: String,
    val username: StringResource,
    val actionText: StringResource,
    val timestamp: StringResource,
    val repoPath: StringResource,
    val prTitle: StringResource,
    val statusText: StringResource,
    val branchName: StringResource,
    val bodyPreview: StringResource
) : ActivityItem()

data class ReleaseItem(
    override val id: String,
    val botName: StringResource,
    val actionText: StringResource,
    val timestamp: StringResource,
    val releaseTitle: StringResource
) : ActivityItem()

data class ExploreUiState(
    val activityFeed: List<ActivityItem> = emptyList()
)
