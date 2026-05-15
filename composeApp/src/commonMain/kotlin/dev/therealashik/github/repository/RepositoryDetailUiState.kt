package dev.therealashik.github.repository

data class RepositoryDetailUiState(
    val ownerAvatarUrl: String = "",
    val ownerName: String = "",
    val repoName: String = "",
    val starsCount: Int = 0,
    val forksCount: Int = 0,
    val isStarred: Boolean = false,
    val issuesCount: Int = 0,
    val pullRequestsCount: Int = 0,
    val discussionsCount: Int = 0,
    val actionsCount: Int = 0, // usually no count for actions but added for consistency if needed
    val releasesCount: Int = 0,
    val latestReleaseVersion: String = "",
    val latestReleaseAge: String = "",
    val contributorsCount: Int = 0,
    val watchersCount: Int = 0,
    val currentBranch: String = "",
    val badges: List<String> = emptyList(),
    val readmeTitle: String = "",
    val readmeBodyPreview: String = ""
)
