package dev.therealashik.github.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.*

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val mockData = HomeUiState.Success(
            myWork = listOf(
                MyWorkItem("mw1", Res.string.my_work_repos, MyWorkItem.IconType.REPOS),
                MyWorkItem("mw2", Res.string.my_work_orgs, MyWorkItem.IconType.ORGS)
            ),
            favorites = listOf(
                FavoriteItem("f1", Res.string.fav_synapsesrc, Res.string.fav_synapseapp, FavoriteItem.IconType.REPO),
                FavoriteItem("f2", Res.string.fav_therealashik, Res.string.fav_jules, FavoriteItem.IconType.AVATAR)
            ),
            shortcuts = listOf(
                ShortcutItem("s1", Res.string.shortcut_issues, Res.string.shortcut_mentioned, ShortcutItem.IconType.ISSUE)
            ),
            recent = listOf(
                RecentItem(
                    id = "r1",
                    repoPath = Res.string.recent_repo_path,
                    title = Res.string.recent_title_1,
                    subtitle = Res.string.recent_subtitle_1,
                    time = Res.string.recent_time_1,
                    commentCount = Res.string.recent_comments_3,
                    isUnread = true,
                    iconType = RecentItem.IconType.PR
                ),
                RecentItem(
                    id = "r2",
                    repoPath = Res.string.recent_repo_path,
                    title = Res.string.recent_title_2,
                    subtitle = Res.string.recent_subtitle_2,
                    time = Res.string.recent_time_2,
                    commentCount = Res.string.recent_comments_12,
                    isUnread = false,
                    iconType = RecentItem.IconType.PR
                )
            )
        )
        _uiState.value = mockData
    }
}
