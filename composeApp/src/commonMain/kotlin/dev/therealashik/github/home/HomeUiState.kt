package dev.therealashik.github.home

import org.jetbrains.compose.resources.StringResource
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.*

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(
        val myWork: List<MyWorkItem>,
        val favorites: List<FavoriteItem>,
        val shortcuts: List<ShortcutItem>,
        val recent: List<RecentItem>
    ) : HomeUiState()
}

data class MyWorkItem(
    val id: String,
    val title: StringResource,
    val iconType: IconType
) {
    enum class IconType { REPOS, ORGS }
}

data class FavoriteItem(
    val id: String,
    val owner: StringResource,
    val repo: StringResource,
    val iconType: IconType
) {
    enum class IconType { REPO, AVATAR }
}

data class ShortcutItem(
    val id: String,
    val category: StringResource,
    val name: StringResource,
    val iconType: IconType
) {
    enum class IconType { ISSUE }
}

data class RecentItem(
    val id: String,
    val repoPath: StringResource,
    val title: StringResource,
    val subtitle: StringResource,
    val time: StringResource,
    val commentCount: StringResource?,
    val isUnread: Boolean,
    val iconType: IconType
) {
    enum class IconType { PR, ISSUE }
}
