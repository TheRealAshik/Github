package dev.therealashik.github.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.therealashik.github.theme.Dimensions
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.activity_section
import github.composeapp.generated.resources.awesome_lists
import github.composeapp.generated.resources.discover_section
import github.composeapp.generated.resources.explore_title
import github.composeapp.generated.resources.filter_activity
import github.composeapp.generated.resources.read_more
import github.composeapp.generated.resources.trending_repos
import github.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExploreScreen(viewModel: ExploreViewModel = viewModel { ExploreViewModel() }) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { ExploreTopBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val state = uiState) {
                is ExploreUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is ExploreUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacingMedium)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = { viewModel.loadData() }) {
                            Text(stringResource(Res.string.retry))
                        }
                    }
                }
                is ExploreUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            DiscoverSection()
                        }
                        item {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = Dimensions.PaddingSmall),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                        item {
                            TrendingReposSection(state.trendingRepos)
                        }
                        item {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = Dimensions.PaddingSmall),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                        item {
                            ActivitySectionHeader()
                        }
                        items(
                            items = state.activityFeed,
                            key = { it.id }
                        ) { item ->
                            when (item) {
                                is ContributionItem -> ContributionCard(item)
                                is ReleaseItem -> ReleaseCard(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.explore_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun DiscoverSection() {
    Column(modifier = Modifier.padding(Dimensions.PaddingMedium)) {
        Text(
            text = stringResource(Res.string.discover_section),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = Dimensions.PaddingMedium)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimensions.PaddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimensions.IconSizeLarge)
                    .clip(RoundedCornerShape(Dimensions.PaddingSmall))
                    .background(MaterialTheme.colorScheme.error), // Flame
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError
                )
            }
            Spacer(modifier = Modifier.width(Dimensions.SpacingMedium))
            Text(
                text = stringResource(Res.string.trending_repos),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimensions.PaddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimensions.IconSizeLarge)
                    .clip(RoundedCornerShape(Dimensions.PaddingSmall))
                    .background(MaterialTheme.colorScheme.tertiary), // Purple-ish
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
            Spacer(modifier = Modifier.width(Dimensions.SpacingMedium))
            Text(
                text = stringResource(Res.string.awesome_lists),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TrendingReposSection(repos: List<TrendingRepoItem>) {
    Column(modifier = Modifier.padding(vertical = Dimensions.PaddingMedium)) {
        Text(
            text = stringResource(Res.string.trending_repos),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = Dimensions.PaddingMedium, end = Dimensions.PaddingMedium, bottom = Dimensions.PaddingMedium)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingMedium),
            modifier = Modifier.fillMaxWidth().padding(horizontal = Dimensions.PaddingMedium)
        ) {
            items(repos, key = { it.id }) { repo ->
                TrendingRepoCard(repo)
            }
        }
    }
}

@Composable
private fun TrendingRepoCard(repo: TrendingRepoItem) {
    Card(
        modifier = Modifier
            .width(Dimensions.TrendingRepoCardWidth)
            .height(Dimensions.TrendingRepoCardHeight),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Dimensions.PaddingMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.PaddingMedium)
        ) {
            Text(
                text = repo.fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))
            Text(
                text = repo.description ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingMedium)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimensions.IconSizeSmall)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.SpacingExtraSmall))
                    Text(
                        text = repo.stars.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (repo.language != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(Dimensions.IndicatorDotSize)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.SpacingExtraSmall))
                        Text(
                            text = repo.language,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivitySectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.PaddingMedium, vertical = Dimensions.PaddingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.activity_section),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = stringResource(Res.string.filter_activity),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ContributionCard(item: ContributionItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clickable { expanded = !expanded }
            .padding(Dimensions.PaddingMedium)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimensions.AvatarSizeMedium)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(Dimensions.IconSizeMedium)
                )
            }
            Spacer(modifier = Modifier.width(Dimensions.SpacingSmall))
            Text(
                text = item.username,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(Dimensions.SpacingExtraSmall))
            Text(
                text = item.actionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = item.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))

        AnimatedVisibility(visible = expanded) {
            // Card Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimensions.PaddingMedium))
                    .border(Dimensions.ThinBorderWidth, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(Dimensions.PaddingMedium))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(Dimensions.PaddingMedium)
            ) {
                Column {
                    Text(
                        text = item.repoPath,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimensions.SpacingExtraSmall))
                    Text(
                        text = item.prTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dimensions.PaddingSmall))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(horizontal = Dimensions.PaddingSmall, vertical = Dimensions.PaddingExtraSmall)
                        ) {
                            Text(
                                text = item.statusText,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dimensions.PaddingSmall))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = Dimensions.PaddingSmall, vertical = Dimensions.PaddingExtraSmall)
                        ) {
                            Text(
                                text = item.branchName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))
                    Text(
                        text = item.bodyPreview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))
                    TextButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.padding(Dimensions.Zero)
                    ) {
                        Text(
                            text = stringResource(Res.string.read_more),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingMedium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier.size(Dimensions.IconSizeSmall),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(Dimensions.PaddingExtraSmall))
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(Dimensions.IconSizeSmall),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(Dimensions.PaddingExtraSmall))
                            Text(
                                text = "2",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReleaseCard(item: ReleaseItem) {
    Column(modifier = Modifier.padding(Dimensions.PaddingMedium)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimensions.AvatarSizeMedium)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(Dimensions.IconSizeMedium)
                )
            }
            Spacer(modifier = Modifier.width(Dimensions.SpacingSmall))
            Text(
                text = item.botName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(Dimensions.SpacingExtraSmall))
            Text(
                text = item.actionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = item.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.SpacingSmall))

        // Card Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimensions.PaddingMedium))
                .border(Dimensions.ThinBorderWidth, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(Dimensions.PaddingMedium))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.ReleaseBannerHeight)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.releaseTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
