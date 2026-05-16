package dev.therealashik.github.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.therealashik.github.Dimens
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onNavigateToRepositories: () -> Unit,
    onNavigateToStarred: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToOrganizations: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = stringResource(Res.string.share)
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(Res.string.settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                        Button(onClick = viewModel::loadData) { Text(stringResource(Res.string.retry)) }
                    }
                }
            }
            is ProfileUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderSection(state)
                    Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                    PopularReposSection(popularRepos = state.popularRepos)
                    Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    NavigationListSection(
                        state = state,
                        onNavigateToRepositories = onNavigateToRepositories,
                        onNavigateToStarred = onNavigateToStarred,
                        onNavigateToOrganizations = onNavigateToOrganizations
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(state: ProfileUiState.Success) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.AvatarSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = state.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

            Column {
                Text(
                    text = state.name ?: state.login,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "@${state.login}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        state.bio?.let {
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (state.statusEmoji != null || state.statusMessage != null) {
            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = Dimens.SpacingSmall, vertical = Dimens.SpacingExtraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.statusEmoji?.let {
                        Text(text = it, style = MaterialTheme.typography.bodyMedium)
                        if (state.statusMessage != null) Spacer(modifier = Modifier.width(Dimens.SpacingExtraSmall))
                    }
                    state.statusMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (state.company != null || state.location != null) {
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            Row(verticalAlignment = Alignment.CenterVertically) {
                state.company?.let { company ->
                    Icon(
                        imageVector = Icons.Outlined.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Dimens.SpacingMedium)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                    Text(
                        text = company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                }
                state.location?.let { location ->
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Dimens.SpacingMedium)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(Dimens.SpacingMedium)
            )
            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
            Text(
                text = stringResource(Res.string.followers_following_format, state.followers, state.following),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PopularReposSection(popularRepos: List<PopularRepo>) {
    Column {
        Text(
            text = stringResource(Res.string.popular_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            popularRepos.forEach { repo ->
                OutlinedCard(
                    modifier = Modifier.width(Dimens.CardWidth).height(Dimens.CardHeight),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(width = Dimens.BorderWidth)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(Dimens.SpacingMedium)
                            .fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = repo.ownerAvatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(Dimens.AvatarSmall)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                            Text(
                                text = repo.owner,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                        Text(
                            text = repo.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (repo.description != null) {
                            Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                            Text(
                                text = repo.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(Dimens.SpacingMedium)
                            )
                            Spacer(modifier = Modifier.width(Dimens.SpacingExtraSmall))
                            Text(
                                text = repo.stars.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (repo.language != null) {
                                Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                                Box(
                                    modifier = Modifier
                                        .size(Dimens.IndicatorSize)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(Dimens.SpacingExtraSmall))
                                Text(
                                    text = repo.language,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationListSection(
    state: ProfileUiState.Success,
    onNavigateToRepositories: () -> Unit,
    onNavigateToStarred: () -> Unit,
    onNavigateToOrganizations: () -> Unit
) {
    Column {
        NavigationItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(Dimens.IconSizeLarge)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            label = stringResource(Res.string.nav_repositories),
            count = state.publicRepos,
            onClick = onNavigateToRepositories
        )
        NavigationItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(Dimens.IconSizeLarge)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            label = stringResource(Res.string.nav_organizations),
            count = state.orgs.size,
            onClick = onNavigateToOrganizations
        )
        NavigationItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(Dimens.IconSizeLarge)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            },
            label = stringResource(Res.string.nav_starred),
            count = state.starredCount,
            onClick = onNavigateToStarred
        )
        NavigationItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(Dimens.IconSizeLarge)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ViewTimeline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            label = stringResource(Res.string.nav_projects),
            count = 0,
            onClick = { /* TODO */ }
        )
    }
}

@Composable
private fun NavigationItem(
    icon: @Composable () -> Unit,
    label: String,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingMediumSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
