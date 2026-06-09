package dev.therealashik.github.home
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import coil3.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.outlined.CallSplit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel { HomeViewModel() },
    onNavigateToProfile: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    HomeScreenContent(state = state, onRetry = viewModel::loadData, onNavigateToProfile = onNavigateToProfile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeUiState, onRetry: () -> Unit = {}, onNavigateToProfile: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.title_home),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Search, contentDescription = stringResource(Res.string.cd_search))
                    }
                    IconButton(onClick = onRetry) {
                        Icon(Icons.Outlined.Refresh, contentDescription = stringResource(Res.string.cd_refresh))
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.AddCircle, contentDescription = stringResource(Res.string.cd_create))
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        if (state is HomeUiState.Success && state.avatarUrl.isNotEmpty()) {
                            AsyncImage(
                                model = state.avatarUrl,
                                contentDescription = stringResource(Res.string.cd_user_avatar),
                                modifier = Modifier
                                    .size(Dimens.IconSizeNormal)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = stringResource(Res.string.cd_user_avatar),
                                modifier = Modifier
                                    .size(Dimens.IconSizeNormal)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                windowInsets = WindowInsets(0)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when (state) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                        Button(onClick = onRetry) { Text(stringResource(Res.string.retry)) }
                    }
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    // My Work
                    item { SectionHeader(title = stringResource(Res.string.section_my_work)) }
                    item {
                        MyWorkRow(
                            icon = Icons.Outlined.AccountBox,
                            title = stringResource(Res.string.my_work_repos),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                    item {
                        MyWorkRow(
                            icon = Icons.Outlined.Menu,
                            title = stringResource(Res.string.my_work_orgs),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    item { HomeDivider() }

                    // Favorites
                    item { SectionHeader(title = stringResource(Res.string.section_favorites)) }
                    if (state.starred.isEmpty()) {
                        item { EmptyHint(stringResource(Res.string.no_starred_repos)) }
                    } else {
                        items(state.starred.take(5)) { StarredRow(it) }
                    }
                    item { HomeDivider() }

                    // Shortcuts
                    item { SectionHeader(title = stringResource(Res.string.section_shortcuts)) }
                    item { ShortcutsRow() }
                    item { HomeDivider() }

                    // Notifications
                    item { SectionHeader(title = stringResource(Res.string.section_recent)) }
                    if (state.notifications.isEmpty()) {
                        item { EmptyHint("No notifications") }
                    } else {
                        items(state.notifications) { NotificationRow(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(onClick = { }, modifier = Modifier.size(Dimens.IconSizeNormal)) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(Res.string.cd_overflow),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun MyWorkRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconSizeLarge)
                .background(color, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(Dimens.IconSizeNormal)
            )
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ShortcutsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconSizeLarge)
                .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(Dimens.IconSizeNormal)
            )
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Column {
            Text(
                text = stringResource(Res.string.shortcuts_issues),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(Res.string.shortcuts_mentioned),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun StarredRow(item: StarredItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.ownerAvatarUrl.isNotEmpty()) {
            AsyncImage(
                model = item.ownerAvatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.IconSizeAvatar)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(Dimens.IconSizeAvatar)
                    .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(Dimens.IconSizeNormal)
                )
            }
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Column {
            Text(
                text = item.ownerLogin,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun RepoRow(item: RepoItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconSizeLarge)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (item.isPrivate) Icons.Outlined.Lock else Icons.Outlined.AccountBox,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.fullName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            item.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
        if (item.stars > 0) {
            Text(
                text = "★ ${item.stars}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OrgRow(item: OrgItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconSizeLarge)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(Dimens.IconSizeNormal)
            )
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Column {
            Text(
                text = item.login,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            item.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun NotificationRow(item: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingMedium),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(Dimens.IconSizeNormal)) {
            Icon(
                imageVector = when (item.type) {
                    "PullRequest" -> Icons.Outlined.AccountBox
                    "Issue" -> Icons.Outlined.Warning
                    else -> Icons.Outlined.Notifications
                },
                contentDescription = null,
                tint = if (item.isUnread) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (item.isUnread) {
                Box(
                    modifier = Modifier
                        .size(Dimens.SpacingMicro)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.TopEnd)
                )
            }
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.repoFullName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingNano))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (item.isUnread) FontWeight.Bold else FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingNano))
            Text(
                text = item.type,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyHint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall)
    )
}

@Composable
private fun HomeDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = Dimens.SpacingExtraLarge + Dimens.SpacingMedium),
        thickness = Dimens.BorderWidthThin,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}






private fun relativeTime(iso: String): String {
    return try {
        // Fallback implementation without kotlinx.datetime since it fails to resolve properly in this environment
        if (iso.length < 19) return ""
        val now = io.ktor.util.date.getTimeMillis()
        // Approximation of ISO parsing using basic math since kotlinx.datetime is not resolving:
        val year = iso.substring(0, 4).toInt()
        val month = iso.substring(5, 7).toInt()
        val day = iso.substring(8, 10).toInt()
        val hour = iso.substring(11, 13).toInt()
        val min = iso.substring(14, 16).toInt()
        val sec = iso.substring(17, 19).toInt()

        val daysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var totalDays = 0L
        for (y in 1970 until year) {
            totalDays += if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) 366 else 365
        }
        val isLeap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
        for (m in 1 until month) {
            totalDays += daysInMonth[m]
            if (m == 2 && isLeap) totalDays++
        }
        totalDays += (day - 1)

        val totalSeconds = (totalDays * 24L * 60L * 60L) + (hour * 60L * 60L) + (min * 60L) + sec
        val millis = totalSeconds * 1000L

        val diff = now - millis
        if (diff < 0) return "just now"

        val diffSeconds = diff / 1000
        val diffMinutes = diffSeconds / 60
        val diffHours = diffMinutes / 60
        val diffDays = diffHours / 24

        when {
            diffSeconds < 60 -> "${diffSeconds}s"
            diffMinutes < 60 -> "${diffMinutes}m"
            diffHours < 24 -> "${diffHours}h"
            diffDays < 7 -> "${diffDays}d"
            else -> "${diffDays / 7}w"
        }
    } catch (e: Exception) {
        ""
    }
}
