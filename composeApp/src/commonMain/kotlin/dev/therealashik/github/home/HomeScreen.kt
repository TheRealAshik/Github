package dev.therealashik.github.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
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
    HomeScreenContent(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeUiState) {
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
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = stringResource(Res.string.cd_search)
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(Res.string.cd_refresh)
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            contentDescription = stringResource(Res.string.cd_create)
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when (state) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                ) {
                    // My Work
                    item {
                        SectionHeader(titleRes = Res.string.section_my_work, showOverflow = true)
                    }
                    items(state.myWork) { item ->
                        MyWorkRow(item)
                    }
                    item { Divider() }

                    // Favorites
                    item {
                        SectionHeader(titleRes = Res.string.section_favorites, showOverflow = true)
                    }
                    items(state.favorites) { item ->
                        FavoriteRow(item)
                    }
                    item { Divider() }

                    // Shortcuts
                    item {
                        SectionHeader(titleRes = Res.string.section_shortcuts, showOverflow = true)
                    }
                    items(state.shortcuts) { item ->
                        ShortcutRow(item)
                    }
                    item { Divider() }

                    // Recent
                    item {
                        SectionHeader(titleRes = Res.string.section_recent, showOverflow = false)
                    }
                    items(state.recent) { item ->
                        RecentRow(item)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(titleRes: org.jetbrains.compose.resources.StringResource, showOverflow: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (showOverflow) {
            IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(Dimens.IconSizeNormal)) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(Res.string.cd_overflow),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MyWorkRow(item: MyWorkItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconColor = when (item.iconType) {
            MyWorkItem.IconType.REPOS -> MaterialTheme.colorScheme.onSurfaceVariant
            MyWorkItem.IconType.ORGS -> MaterialTheme.colorScheme.tertiary
        }

        Box(
            modifier = Modifier
                .size(Dimens.IconSizeLarge)
                .background(iconColor.copy(alpha = 0.2f), MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person, // Placeholder, usually would be a specific repo/org icon
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
        }

        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun FavoriteRow(item: FavoriteItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.iconType == FavoriteItem.IconType.REPO) {
            Box(
                modifier = Modifier
                    .size(Dimens.IconSizeLarge)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimens.IconSizeSmall)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
        } else {
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
        }

        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

        Column {
            Text(
                text = stringResource(item.owner),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(item.repo),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun ShortcutRow(item: ShortcutItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconSizeLarge)
                .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Search, // Placeholder for eye/issue icon
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
        }

        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

        Column {
            Text(
                text = stringResource(item.category),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(item.name),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun RecentRow(item: RecentItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingMedium),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(Dimens.IconSizeNormal)
        ) {
            Icon(
                imageVector = Icons.Outlined.AddCircle, // Placeholder for branch/PR icon
                contentDescription = null,
                tint = if (item.iconType == RecentItem.IconType.PR) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
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
                    text = stringResource(item.time),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingNano))

            Text(
                text = stringResource(item.title),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingNano))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(item.subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                item.commentCount?.let { countRes ->
                    Box(
                        modifier = Modifier
                            .height(Dimens.BadgeHeight)
                            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
                            .padding(horizontal = Dimens.BadgePaddingHorizontal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(countRes),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Divider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = Dimens.SpacingExtraLarge + Dimens.SpacingMedium),
        thickness = Dimens.BorderWidthThin,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}
