package dev.therealashik.github.pullrequest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.assignee_filter
import github.composeapp.generated.resources.author_filter
import github.composeapp.generated.resources.back
import github.composeapp.generated.resources.checks_format
import github.composeapp.generated.resources.comments_count_format
import github.composeapp.generated.resources.create
import github.composeapp.generated.resources.draft_filter
import github.composeapp.generated.resources.label_filter
import github.composeapp.generated.resources.open_filter
import github.composeapp.generated.resources.pr_number_format
import github.composeapp.generated.resources.pull_requests
import github.composeapp.generated.resources.search
import org.jetbrains.compose.resources.stringResource
import dev.therealashik.github.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRequestListScreen(
    uiState: PullRequestListUiState,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.ownerName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(Res.string.pull_requests),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
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
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(Res.string.search)
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(Res.string.create)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterChipsRow()
            HorizontalDivider()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.pullRequests) { pr ->
                    PullRequestListItem(item = pr)
                    HorizontalDivider(modifier = Modifier.padding(start = Dimens.SpacingMassive))
                }
            }
        }
    }
}

@Composable
fun FilterChipsRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        val filters = listOf(
            Res.string.open_filter,
            Res.string.draft_filter,
            Res.string.label_filter,
            Res.string.author_filter,
            Res.string.assignee_filter
        )
        items(filters) { filterRes ->
            SuggestionChip(
                onClick = { /* TODO */ },
                label = { Text(stringResource(filterRes)) }
            )
        }
    }
}

@Composable
fun PullRequestListItem(item: PullRequestItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SpacingLarge)
    ) {
        Box(modifier = Modifier.padding(top = Dimens.SpacingExtraSmall)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.CallSplit, // Using CallSplit as placeholder for PR branching arrows
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, // green PR icon in custom theme
                modifier = Modifier.size(Dimens.IconSizeMedium)
            )
            if (item.isUnread) {
                Box(
                    modifier = Modifier
                        .size(Dimens.IconSizeExtraSmall)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary) // Blue dot for unread
                        .align(Alignment.TopEnd)
                )
            }
        }
        Spacer(modifier = Modifier.width(Dimens.SpacingLarge))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
            Text(
                text = "${item.author} ${stringResource(Res.string.pr_number_format, item.number)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
                if (item.checksSummary.isNotEmpty()) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(stringResource(Res.string.checks_format, item.checksSummary)) }
                    )
                }
                if (item.commentsCount > 0) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(stringResource(Res.string.comments_count_format, item.commentsCount)) }
                    )
                }
            }
        }
    }
}
