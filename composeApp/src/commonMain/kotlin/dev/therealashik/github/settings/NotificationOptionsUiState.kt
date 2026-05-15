package dev.therealashik.github.settings

data class NotificationOptionsUiState(
    val directMentions: Boolean = true,
    val reviewRequested: Boolean = true,
    val assigned: Boolean = true,
    val deploymentReview: Boolean = true,
    val pullRequestReview: Boolean = true,
    val workflowRuns: Boolean = true,
    val failedWorkflowsOnly: Boolean = true,
    val liveAgentUpdates: Boolean = true,
    val leftSwipeAction: String = "Mark as done",
    val rightSwipeAction: String = "Unsubscribe"
)
