package dev.therealashik.github.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationOptionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotificationOptionsViewModel = viewModel { NotificationOptionsViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.notification_options_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(Res.string.content_description_back))
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
            // General Section
            item {
                SettingsSectionHeader(stringResource(Res.string.notification_options_section_general))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO */ }
                        .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.notification_options_system_options),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(Res.string.notification_options_system_options_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(Res.string.content_description_system_settings),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SettingsRow(
                    title = stringResource(Res.string.notification_options_working_hours),
                    subtitle = stringResource(Res.string.notification_options_working_hours_desc),
                    onClick = { /* TODO */ }
                )
                SectionDivider()
            }

            // Push Notifications Types
            item {
                SettingsSectionHeader(stringResource(Res.string.notification_options_section_push_types))
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_direct_mentions),
                    checked = uiState.directMentions,
                    onCheckedChange = { viewModel.toggleDirectMentions() }
                )
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_review_requested),
                    checked = uiState.reviewRequested,
                    onCheckedChange = { viewModel.toggleReviewRequested() }
                )
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_assigned),
                    checked = uiState.assigned,
                    onCheckedChange = { viewModel.toggleAssigned() }
                )
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_deployment_review),
                    checked = uiState.deploymentReview,
                    onCheckedChange = { viewModel.toggleDeploymentReview() }
                )
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_pull_request_review),
                    checked = uiState.pullRequestReview,
                    onCheckedChange = { viewModel.togglePullRequestReview() }
                )
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_workflow_runs),
                    checked = uiState.workflowRuns,
                    onCheckedChange = { viewModel.toggleWorkflowRuns() }
                )
                SettingsToggleRow(
                    title = stringResource(Res.string.notification_options_failed_workflows),
                    checked = uiState.failedWorkflowsOnly,
                    onCheckedChange = { viewModel.toggleFailedWorkflowsOnly() }
                )
                SectionDivider()
            }

            // Live Notifications
            item {
                SettingsSectionHeader(stringResource(Res.string.notification_options_section_live))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = SettingsTokens.PaddingLarge)) {
                        Text(
                            text = stringResource(Res.string.notification_options_live_agent),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(Res.string.notification_options_live_agent_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.liveAgentUpdates,
                        onCheckedChange = { viewModel.toggleLiveAgentUpdates() }
                    )
                }
                SectionDivider()
            }

            // Swipe Options
            item {
                SettingsSectionHeader(stringResource(Res.string.notification_options_section_swipe))

                // Left Swipe
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.notification_options_left_swipe),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = uiState.leftSwipeAction,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { /* TODO */ }) {
                        Text(
                            text = stringResource(Res.string.notification_options_change),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                // Right Swipe
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.notification_options_right_swipe),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = uiState.rightSwipeAction,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { /* TODO */ }) {
                        Text(
                            text = stringResource(Res.string.notification_options_change),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                // Swipe Preview Placeholder
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SettingsTokens.PaddingLarge),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SettingsTokens.PaddingLarge),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsOff,
                            contentDescription = stringResource(Res.string.content_description_swipe_preview_icon),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(SettingsTokens.PaddingLarge))
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(SettingsTokens.SwipePreviewLineHeight)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                            )
                            Spacer(modifier = Modifier.height(SettingsTokens.PaddingMedium))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.4f)
                                    .height(SettingsTokens.SwipePreviewLineHeight)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = SettingsTokens.PaddingLarge, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
