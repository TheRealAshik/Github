package dev.therealashik.github.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.explore_action_contributed
import github.composeapp.generated.resources.explore_action_published
import org.jetbrains.compose.resources.getString

class ExploreViewModel : ViewModel() {

    private val apiClient = GitHubApiClient(createTokenStorage())

    private val _uiState = MutableStateFlow<ExploreUiState>(ExploreUiState.Loading)
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = ExploreUiState.Loading

            val userResult = apiClient.getAuthenticatedUser()
            if (userResult.isFailure) {
                _uiState.value = ExploreUiState.Error(userResult.exceptionOrNull()?.message ?: "Failed to get user")
                return@launch
            }
            val user = userResult.getOrThrow()

            val trendingDeferred = async { apiClient.getTrendingRepos() }
            val eventsDeferred = async { apiClient.getReceivedEvents(user.login) }

            val trendingResult = trendingDeferred.await()
            val eventsResult = eventsDeferred.await()

            if (trendingResult.isFailure && eventsResult.isFailure) {
                _uiState.value = ExploreUiState.Error(trendingResult.exceptionOrNull()?.message ?: "Failed to load explore data")
                return@launch
            }

            val trendingRepos = trendingResult.getOrDefault(dev.therealashik.github.data.SearchResult(emptyList())).items.map { repo ->
                TrendingRepoItem(
                    id = repo.id.toString(),
                    fullName = repo.fullName,
                    description = repo.description,
                    stars = repo.stars,
                    language = repo.language
                )
            }

            val activityFeed = eventsResult.getOrDefault(emptyList()).mapNotNull { event ->
                when (event.type) {
                    "PullRequestEvent" -> {
                        val pr = event.payload?.pullRequest
                        if (pr != null) {
                            ContributionItem(
                                id = event.id,
                                username = event.actor.login,
                                actionText = getString(Res.string.explore_action_contributed, event.repo.name),
                                timestamp = event.createdAt,
                                repoPath = event.repo.name,
                                prTitle = pr.title,
                                statusText = pr.state,
                                branchName = pr.head.ref,
                                bodyPreview = pr.body?.take(120) ?: ""
                            )
                        } else null
                    }
                    "ReleaseEvent" -> {
                        val release = event.payload?.release
                        if (release != null) {
                            ReleaseItem(
                                id = event.id,
                                botName = event.actor.login,
                                actionText = getString(Res.string.explore_action_published),
                                timestamp = event.createdAt,
                                releaseTitle = release.name ?: release.tagName
                            )
                        } else null
                    }
                    else -> null
                }
            }

            _uiState.value = ExploreUiState.Success(
                trendingRepos = trendingRepos,
                activityFeed = activityFeed
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
