package dev.therealashik.github.inbox

import androidx.lifecycle.ViewModel
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.inbox_mock_repo_1
import github.composeapp.generated.resources.inbox_mock_title_1
import github.composeapp.generated.resources.inbox_mock_subtitle_1
import github.composeapp.generated.resources.inbox_mock_time_1
import github.composeapp.generated.resources.inbox_mock_repo_2
import github.composeapp.generated.resources.inbox_mock_title_2
import github.composeapp.generated.resources.inbox_mock_subtitle_2
import github.composeapp.generated.resources.inbox_mock_time_2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InboxViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InboxUiState())
    val uiState: StateFlow<InboxUiState> = _uiState.asStateFlow()

    init {
        loadMocks()
    }

    private fun loadMocks() {
        val mocks = listOf(
            NotificationItem(
                id = "1",
                type = NotificationType.PULL_REQUEST,
                isUnread = true,
                repoPath = Res.string.inbox_mock_repo_1,
                timestamp = Res.string.inbox_mock_time_1,
                title = Res.string.inbox_mock_title_1,
                subtitle = Res.string.inbox_mock_subtitle_1,
                commentCount = 2
            ),
            NotificationItem(
                id = "2",
                type = NotificationType.RELEASE,
                isUnread = false,
                repoPath = Res.string.inbox_mock_repo_2,
                timestamp = Res.string.inbox_mock_time_2,
                title = Res.string.inbox_mock_title_2,
                subtitle = Res.string.inbox_mock_subtitle_2,
                commentCount = null
            )
        )
        _uiState.value = InboxUiState(notifications = mocks)
    }

    fun markAsDone(id: String) {
        _uiState.value = _uiState.value.copy(
            notifications = _uiState.value.notifications.filter { it.id != id }
        )
    }

    fun unsubscribe(id: String) {
        _uiState.value = _uiState.value.copy(
            notifications = _uiState.value.notifications.filter { it.id != id }
        )
    }
}
