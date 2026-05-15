package dev.therealashik.github.repository

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RepositoryDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        RepositoryDetailUiState(
            ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1?v=4", // placeholder
            ownerName = "google",
            repoName = "accompanist",
            starsCount = 2,
            forksCount = 1,
            isStarred = false,
            issuesCount = 11,
            pullRequestsCount = 4,
            discussionsCount = 3,
            actionsCount = 0,
            releasesCount = 5,
            latestReleaseVersion = "v0.32.0",
            latestReleaseAge = "2 years ago",
            contributorsCount = 1,
            watchersCount = 0,
            currentBranch = "main",
            badges = listOf("build: passing", "kotlin: 1.9.0", "compose: 1.5.0", "backend: Ktor", "license: Apache", "platform: Android"),
            readmeTitle = "Accompanist",
            readmeBodyPreview = "A collection of extension libraries for Jetpack Compose."
        )
    )
    val uiState: StateFlow<RepositoryDetailUiState> = _uiState.asStateFlow()

    fun toggleStar() {
        _uiState.value = _uiState.value.copy(
            isStarred = !_uiState.value.isStarred,
            starsCount = if (_uiState.value.isStarred) _uiState.value.starsCount - 1 else _uiState.value.starsCount + 1
        )
    }
}
