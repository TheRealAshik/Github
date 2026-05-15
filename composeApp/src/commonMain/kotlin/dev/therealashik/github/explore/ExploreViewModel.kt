package dev.therealashik.github.explore

import androidx.lifecycle.ViewModel
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.explore_mock_user_1
import github.composeapp.generated.resources.explore_mock_action_1
import github.composeapp.generated.resources.explore_mock_time_1
import github.composeapp.generated.resources.explore_mock_repo_1
import github.composeapp.generated.resources.explore_mock_pr_title_1
import github.composeapp.generated.resources.explore_mock_status_merged
import github.composeapp.generated.resources.explore_mock_branch_1
import github.composeapp.generated.resources.explore_mock_pr_body_1
import github.composeapp.generated.resources.explore_mock_bot
import github.composeapp.generated.resources.explore_mock_action_release
import github.composeapp.generated.resources.explore_mock_time_release
import github.composeapp.generated.resources.explore_mock_release_title
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExploreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadMocks()
    }

    private fun loadMocks() {
        val mocks = listOf(
            ContributionItem(
                id = "1",
                username = Res.string.explore_mock_user_1,
                actionText = Res.string.explore_mock_action_1,
                timestamp = Res.string.explore_mock_time_1,
                repoPath = Res.string.explore_mock_repo_1,
                prTitle = Res.string.explore_mock_pr_title_1,
                statusText = Res.string.explore_mock_status_merged,
                branchName = Res.string.explore_mock_branch_1,
                bodyPreview = Res.string.explore_mock_pr_body_1
            ),
            ReleaseItem(
                id = "2",
                botName = Res.string.explore_mock_bot,
                actionText = Res.string.explore_mock_action_release,
                timestamp = Res.string.explore_mock_time_release,
                releaseTitle = Res.string.explore_mock_release_title
            )
        )
        _uiState.value = ExploreUiState(activityFeed = mocks)
    }
}
