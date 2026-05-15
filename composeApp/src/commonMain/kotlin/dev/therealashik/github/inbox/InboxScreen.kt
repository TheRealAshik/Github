package dev.therealashik.github.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.therealashik.github.theme.Dimensions
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.filter_focused
import github.composeapp.generated.resources.filter_inbox
import github.composeapp.generated.resources.filter_repository
import github.composeapp.generated.resources.filter_unread
import github.composeapp.generated.resources.inbox_title
import github.composeapp.generated.resources.more_options
import github.composeapp.generated.resources.swipe_mark_done
import github.composeapp.generated.resources.swipe_unsubscribe
import org.jetbrains.compose.resources.stringResource

@Composable
fun InboxScreen(viewModel: InboxViewModel = viewModel { InboxViewModel() }) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { InboxTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FilterChipsRow()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiState.notifications,
                    key = { it.id }
                ) { notification ->
                    NotificationSwipeItem(
                        item = notification,
                        onMarkDone = { viewModel.markAsDone(notification.id) },
                        onUnsubscribe = { viewModel.unsubscribe(notification.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InboxTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.inbox_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(Res.string.more_options)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun FilterChipsRow() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.PaddingMedium, vertical = Dimensions.PaddingSmall),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingSmall)
    ) {
        item {
            FilterChip(
                selected = true,
                onClick = { /* TODO */ },
                label = { Text(stringResource(Res.string.filter_inbox)) }
            )
        }
        item {
            FilterChip(
                selected = false,
                onClick = { /* TODO */ },
                label = { Text(stringResource(Res.string.filter_focused)) }
            )
        }
        item {
            FilterChip(
                selected = false,
                onClick = { /* TODO */ },
                label = { Text(stringResource(Res.string.filter_unread)) }
            )
        }
        item {
            FilterChip(
                selected = false,
                onClick = { /* TODO */ },
                label = { Text(stringResource(Res.string.filter_repository)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationSwipeItem(
    item: NotificationItem,
    onMarkDone: () -> Unit,
    onUnsubscribe: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onUnsubscribe()
                    true
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onMarkDone()
                    true
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.errorContainer
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.primaryContainer
                SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.background
            }
            val alignment = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                SwipeToDismissBoxValue.Settled -> Alignment.Center
            }
            val icon = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Outlined.NotificationsOff
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Check
                SwipeToDismissBoxValue.Settled -> Icons.Default.Check
            }
            val textRes = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Res.string.swipe_unsubscribe
                SwipeToDismissBoxValue.EndToStart -> Res.string.swipe_mark_done
                SwipeToDismissBoxValue.Settled -> Res.string.swipe_mark_done
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = Dimensions.PaddingLarge),
                contentAlignment = alignment
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                        Icon(icon, contentDescription = null)
                        Spacer(Modifier.width(Dimensions.SpacingSmall))
                        Text(stringResource(textRes))
                    } else if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                        Text(stringResource(textRes))
                        Spacer(Modifier.width(Dimensions.SpacingSmall))
                        Icon(icon, contentDescription = null)
                    }
                }
            }
        },
        content = {
            NotificationRow(item)
        }
    )
}

@Composable
private fun NotificationRow(item: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimensions.PaddingMedium),
        verticalAlignment = Alignment.Top
    ) {
        // Left: Icon + Unread Dot
        Box(modifier = Modifier.padding(end = Dimensions.PaddingMedium)) {
            val icon = if (item.type == NotificationType.PULL_REQUEST) {
                Icons.Outlined.AltRoute
            } else {
                Icons.Outlined.LocalOffer
            }
            val tint = if (item.type == NotificationType.PULL_REQUEST) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(Dimensions.IconSizeMedium)
            )
            if (item.isUnread) {
                Box(
                    modifier = Modifier
                        .size(Dimensions.IndicatorDotSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.TopEnd)
                )
            }
        }

        // Center: Content
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(item.repoPath),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(item.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = stringResource(item.title),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            item.subtitle?.let {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Right: Comment Count Badge
        if (item.commentCount != null) {
            Box(
                modifier = Modifier
                    .padding(start = Dimensions.PaddingSmall)
                    .clip(RoundedCornerShape(Dimensions.PaddingSmall))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = Dimensions.PaddingSmall, vertical = Dimensions.PaddingExtraSmall)
            ) {
                Text(
                    text = item.commentCount.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
