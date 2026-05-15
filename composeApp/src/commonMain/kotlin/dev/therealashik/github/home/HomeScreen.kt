package dev.therealashik.github.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
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
fun HomeScreen(viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel { HomeViewModel() }) {
    val state by viewModel.uiState.collectAsState()
    HomeScreenContent(state = state, onRetry = viewModel::loadData)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeUiState, onRetry: () -> Unit = {}) {
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
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = stringResource(Res.string.cd_user_avatar),
                            modifier = Modifier
                                .size(Dimens.IconSizeNormal)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                    // Repositories
                    item { SectionHeader(title = stringResource(Res.string.section_my_work)) }
                    items(state.repos) { RepoRow(it) }
                    item { HomeDivider() }

                    // Organizations
                    item { SectionHeader(title = "Organizations") }
                    if (state.orgs.isEmpty()) {
                        item { EmptyHint("No organizations") }
                    } else {
                        items(state.orgs) { OrgRow(it) }
                    }
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
