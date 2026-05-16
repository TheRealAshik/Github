package dev.therealashik.github.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val apiClient = GitHubApiClient(createTokenStorage())

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val reposDeferred = async { apiClient.getUserRepos() }
            val orgsDeferred = async { apiClient.getUserOrgs() }
            val notificationsDeferred = async { apiClient.getNotifications() }
            val starredDeferred = async { apiClient.getStarredRepos(perPage = 5) }
            val userDeferred = async { apiClient.getAuthenticatedUser() }

            val repos = reposDeferred.await()
            val orgs = orgsDeferred.await()
            val notifications = notificationsDeferred.await()
            val starred = starredDeferred.await()
            val user = userDeferred.await()

            if (repos.isFailure && orgs.isFailure && notifications.isFailure) {
                _uiState.value = HomeUiState.Error(repos.exceptionOrNull()?.message ?: "Failed to load data")
                return@launch
            }

            _uiState.value = HomeUiState.Success(
                avatarUrl = user.getOrNull()?.avatarUrl ?: "",
                starred = starred.getOrDefault(emptyList()).map { repo ->
                    StarredItem(
                        id = repo.id,
                        name = repo.name,
                        ownerLogin = repo.owner?.login ?: "",
                        ownerAvatarUrl = repo.owner?.avatarUrl ?: ""
                    )
                },
                repos = repos.getOrDefault(emptyList()).map { repo ->
                    RepoItem(
                        id = repo.id,
                        name = repo.name,
                        fullName = repo.fullName,
                        description = repo.description,
                        language = repo.language,
                        stars = repo.stars,
                        isPrivate = repo.private,
                        updatedAt = repo.updatedAt
                    )
                },
                orgs = orgs.getOrDefault(emptyList()).map { org ->
                    OrgItem(
                        id = org.id,
                        login = org.login,
                        avatarUrl = org.avatarUrl,
                        description = org.description
                    )
                },
                notifications = notifications.getOrDefault(emptyList()).map { n ->
                    NotificationItem(
                        id = n.id,
                        repoFullName = n.repository.fullName,
                        title = n.subject.title,
                        type = n.subject.type,
                        isUnread = n.unread,
                        updatedAt = n.updatedAt
                    )
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
