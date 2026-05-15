package dev.therealashik.github.pullrequest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PullRequestListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        PullRequestListUiState(
            ownerName = "google",
            repoName = "accompanist",
            pullRequests = listOf(
                PullRequestItem(
                    id = 1,
                    title = "Update Compose to 1.6.0",
                    number = 1532,
                    timestamp = "2 hours ago",
                    author = "chrisbanes",
                    isUnread = true,
                    checksSummary = "3/3",
                    commentsCount = 5
                ),
                PullRequestItem(
                    id = 2,
                    title = "Fix navigation animation glitch",
                    number = 1530,
                    timestamp = "yesterday",
                    author = "johndoe",
                    isUnread = false,
                    checksSummary = "2/3",
                    commentsCount = 2
                ),
                PullRequestItem(
                    id = 3,
                    title = "Add new indicator style",
                    number = 1525,
                    timestamp = "3 days ago",
                    author = "janedoe",
                    isUnread = false,
                    checksSummary = "3/3",
                    commentsCount = 12
                )
            )
        )
    )
    val uiState: StateFlow<PullRequestListUiState> = _uiState.asStateFlow()
}
