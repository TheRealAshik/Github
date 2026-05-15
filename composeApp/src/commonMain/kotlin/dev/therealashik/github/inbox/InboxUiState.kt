package dev.therealashik.github.inbox

import org.jetbrains.compose.resources.StringResource

enum class NotificationType {
    PULL_REQUEST,
    RELEASE
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val isUnread: Boolean,
    val repoPath: StringResource,
    val timestamp: StringResource,
    val title: StringResource,
    val subtitle: StringResource?,
    val commentCount: Int? = null
)

data class InboxUiState(
    val notifications: List<NotificationItem> = emptyList()
)
