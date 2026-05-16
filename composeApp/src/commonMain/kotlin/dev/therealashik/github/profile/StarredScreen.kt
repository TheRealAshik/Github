package dev.therealashik.github.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import dev.therealashik.github.Dimens
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.content_description_back
import github.composeapp.generated.resources.retry
import github.composeapp.generated.resources.starred_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarredScreen(
    viewModel: StarredViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.starred_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.content_description_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is StarredUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is StarredUiState.Error -> {
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
            is StarredUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(vertical = Dimens.SpacingMedium),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
                ) {
                    items(state.starredRepos) { repo ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.SpacingMedium),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimens.SpacingMedium)
                            ) {
                                Text(
                                    text = repo.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (repo.description != null) {
                                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                                    Text(
                                        text = repo.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(Dimens.IconSizeSmall)
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
    }
}
