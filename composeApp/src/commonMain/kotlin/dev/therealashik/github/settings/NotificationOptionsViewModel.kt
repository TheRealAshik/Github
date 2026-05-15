package dev.therealashik.github.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationOptionsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationOptionsUiState())
    val uiState: StateFlow<NotificationOptionsUiState> = _uiState.asStateFlow()

    fun toggleDirectMentions() {
        _uiState.update { it.copy(directMentions = !it.directMentions) }
    }

    fun toggleReviewRequested() {
        _uiState.update { it.copy(reviewRequested = !it.reviewRequested) }
    }

    fun toggleAssigned() {
        _uiState.update { it.copy(assigned = !it.assigned) }
    }

    fun toggleDeploymentReview() {
        _uiState.update { it.copy(deploymentReview = !it.deploymentReview) }
    }

    fun togglePullRequestReview() {
        _uiState.update { it.copy(pullRequestReview = !it.pullRequestReview) }
    }

    fun toggleWorkflowRuns() {
        _uiState.update { it.copy(workflowRuns = !it.workflowRuns) }
    }

    fun toggleFailedWorkflowsOnly() {
        _uiState.update { it.copy(failedWorkflowsOnly = !it.failedWorkflowsOnly) }
    }

    fun toggleLiveAgentUpdates() {
        _uiState.update { it.copy(liveAgentUpdates = !it.liveAgentUpdates) }
    }
}
