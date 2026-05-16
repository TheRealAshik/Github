package dev.therealashik.github.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.therealashik.github.data.GitHubApiClient
import dev.therealashik.github.data.createTokenStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val apiClient = GitHubApiClient(createTokenStorage())

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            val userResult = apiClient.getAuthenticatedUser()
            if (userResult.isFailure) {
                _uiState.value = ProfileUiState.Error(userResult.exceptionOrNull()?.message ?: "Failed to load profile")
                return@launch
            }
            val user = userResult.getOrThrow()

            val reposDeferred = async { apiClient.getUserRepos(perPage = 6) }
            val orgsDeferred = async { apiClient.getUserOrgs() }
            val starredDeferred = async { apiClient.getStarredRepos(perPage = 1) }
            val statusDeferred = async { apiClient.getUserStatus() }

            val repos = reposDeferred.await().getOrDefault(emptyList())
            val orgs = orgsDeferred.await().getOrDefault(emptyList())
            val starred = starredDeferred.await().getOrDefault(emptyList())
            val status = statusDeferred.await().getOrNull()

            _uiState.value = ProfileUiState.Success(
                login = user.login,
                name = user.name,
                avatarUrl = user.avatarUrl,
                bio = user.bio,
                company = user.company,
                location = user.location,
                followers = user.followers,
                following = user.following,
                publicRepos = user.publicRepos,
                popularRepos = repos.sortedByDescending { it.stars }.take(6).map { repo ->
                    PopularRepo(
                        id = repo.id,
                        owner = user.login,
                        name = repo.name,
                        description = repo.description,
                        stars = repo.stars,
                        language = repo.language
                    )
                },
                orgs = orgs.map { OrgSummary(it.id, it.login, it.avatarUrl) },
                starredCount = starred.size,
                statusEmoji = status?.emoji,
                statusMessage = status?.message
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}
