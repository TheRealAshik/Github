package dev.therealashik.github.repository

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.actions
import github.composeapp.generated.resources.add_to_list
import github.composeapp.generated.resources.back
import github.composeapp.generated.resources.change
import github.composeapp.generated.resources.code
import github.composeapp.generated.resources.commits
import github.composeapp.generated.resources.contributors
import github.composeapp.generated.resources.create
import github.composeapp.generated.resources.current_branch
import github.composeapp.generated.resources.discussions
import github.composeapp.generated.resources.edit
import github.composeapp.generated.resources.fork
import github.composeapp.generated.resources.forks_count_format
import github.composeapp.generated.resources.issues
import github.composeapp.generated.resources.latest
import github.composeapp.generated.resources.more_options
import github.composeapp.generated.resources.pull_requests
import github.composeapp.generated.resources.readme_md
import github.composeapp.generated.resources.releases
import github.composeapp.generated.resources.star
import github.composeapp.generated.resources.stars_count_format
import github.composeapp.generated.resources.watch
import github.composeapp.generated.resources.watchers
import org.jetbrains.compose.resources.stringResource
import dev.therealashik.github.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    uiState: RepositoryDetailUiState,
    onNavigateBack: () -> Unit = {},
    onStarClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(Res.string.create)
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(Res.string.more_options)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                HeaderSection(uiState = uiState, onStarClick = onStarClick)
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.SpacingMedium))
            }
            item {
                NavigationListSection(uiState = uiState)
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.SpacingMedium))
            }
            item {
                BranchSection(uiState = uiState)
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.SpacingMedium))
            }
            item {
                MoreRowsSection()
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.SpacingMedium))
            }
            item {
                ReadmeSection(uiState = uiState)
            }
        }
    }
}

@Composable
fun HeaderSection(uiState: RepositoryDetailUiState, onStarClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = Dimens.SpacingLarge)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Placeholder for Avatar
            Box(
                modifier = Modifier
                    .size(Dimens.IconSizeMedium)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
            Text(
                text = uiState.ownerName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        Text(
            text = uiState.repoName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        Row {
            Text(
                text = stringResource(Res.string.stars_count_format, uiState.starsCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(Dimens.SpacingLarge))
            Text(
                text = stringResource(Res.string.forks_count_format, uiState.forksCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            val buttonModifier = Modifier.weight(1f)
            OutlinedButton(onClick = onStarClick, modifier = buttonModifier) {
                Icon(
                    imageVector = if (uiState.isStarred) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = stringResource(Res.string.star),
                    tint = if (uiState.isStarred) MaterialTheme.colorScheme.tertiary else LocalContentColor.current
                )
            }
            OutlinedButton(onClick = { /* TODO */ }, modifier = buttonModifier) {
                Text(
                    text = stringResource(Res.string.add_to_list),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            OutlinedButton(onClick = { /* TODO */ }, modifier = buttonModifier) {
                Text(
                    text = stringResource(Res.string.fork),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            OutlinedButton(onClick = { /* TODO */ }, modifier = buttonModifier) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(Res.string.watch)
                )
            }
        }
    }
}

@Composable
fun NavigationListSection(uiState: RepositoryDetailUiState) {
    Column {
        NavigationListItem(
            icon = Icons.Outlined.Info,
            label = stringResource(Res.string.issues),
            count = uiState.issuesCount,
            iconTint = MaterialTheme.colorScheme.primary
        )
        NavigationListItem(
            icon = Icons.Default.Code, // Need PR icon, placeholder for now
            label = stringResource(Res.string.pull_requests),
            count = uiState.pullRequestsCount,
            iconTint = MaterialTheme.colorScheme.secondary
        )
        NavigationListItem(
            icon = Icons.Outlined.ChatBubbleOutline,
            label = stringResource(Res.string.discussions),
            count = uiState.discussionsCount,
            iconTint = MaterialTheme.colorScheme.tertiary
        )
        NavigationListItem(
            icon = Icons.Outlined.PlayCircleOutline,
            label = stringResource(Res.string.actions),
            count = null,
            iconTint = MaterialTheme.colorScheme.secondary
        )
        NavigationListItem(
            icon = Icons.Default.PlayArrow, // Need tag icon, placeholder
            label = stringResource(Res.string.releases),
            count = uiState.releasesCount,
            iconTint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (uiState.releasesCount > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(Dimens.SpacingMedium)
            ) {
                Row(
                    modifier = Modifier.padding(Dimens.SpacingMediumLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.latest),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                RoundedCornerShape(Dimens.SpacingSmall)
                            )
                            .padding(horizontal = Dimens.SpacingMediumSmall, vertical = Dimens.SpacingExtraSmall)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                    Text(
                        text = uiState.latestReleaseVersion,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                    Text(
                        text = uiState.latestReleaseAge,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        NavigationListItem(
            icon = Icons.Outlined.Person,
            label = stringResource(Res.string.contributors),
            count = uiState.contributorsCount,
            iconTint = MaterialTheme.colorScheme.secondary
        )
        NavigationListItem(
            icon = Icons.Outlined.AccountCircle, // Placeholder for watcher icon
            label = stringResource(Res.string.watchers),
            count = uiState.watchersCount,
            iconTint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun NavigationListItem(
    icon: ImageVector,
    label: String,
    count: Int?,
    iconTint: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMediumLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(Dimens.IconSizeMedium)
        )
        Spacer(modifier = Modifier.width(Dimens.SpacingLarge))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (count != null) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BranchSection(uiState: RepositoryDetailUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.current_branch),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = uiState.currentBranch,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        TextButton(onClick = { /* TODO */ }) {
            Text(
                text = stringResource(Res.string.change),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun MoreRowsSection() {
    Column {
        NavigationListItem(
            icon = Icons.Default.Code,
            label = stringResource(Res.string.code),
            count = null,
            iconTint = MaterialTheme.colorScheme.onSurface
        )
        NavigationListItem(
            icon = Icons.Outlined.Info, // Placeholder for commit icon
            label = stringResource(Res.string.commits),
            count = null,
            iconTint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReadmeSection(uiState: RepositoryDetailUiState) {
    Column(modifier = Modifier.padding(horizontal = Dimens.SpacingLarge)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.readme_md),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO */ }) {
                Text(
                    text = stringResource(Res.string.edit),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            uiState.badges.forEach { badge ->
                SuggestionChip(
                    onClick = { },
                    label = { Text(badge, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        Text(
            text = uiState.readmeTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        Text(
            text = uiState.readmeBodyPreview,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))
    }
}
