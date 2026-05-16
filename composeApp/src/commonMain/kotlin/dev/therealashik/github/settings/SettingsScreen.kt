package dev.therealashik.github.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import github.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNotificationOptions: () -> Unit,
    onNavigateToCodeOptions: () -> Unit,
    onNavigateToAddPat: () -> Unit,
    viewModel: SettingsViewModel = viewModel { SettingsViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAccountsSheet by remember { mutableStateOf(false) }

    // Refresh token state when returning to this screen
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) viewModel.refresh()
    }
    val uriHandler = LocalUriHandler.current

    if (showAccountsSheet) {
        AccountsSheet(
            accounts = uiState.accounts,
            onDismiss = { showAccountsSheet = false },
            onAddAccount = { showAccountsSheet = false; onNavigateToAddPat() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.settings_title),
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
            // Notifications Section
            item {
                SettingsSectionHeader(stringResource(Res.string.settings_section_notifications))
                SettingsRow(
                    title = stringResource(Res.string.settings_notification_options),
                    onClick = onNavigateToNotificationOptions
                )
                SectionDivider()
            }

            // General Section
            item {
                SettingsSectionHeader(stringResource(Res.string.settings_section_general))
                SettingsRow(
                    title = stringResource(Res.string.settings_theme),
                    subtitle = uiState.themeValue,
                    onClick = { /* TODO */ }
                )
                SettingsRow(
                    title = stringResource(Res.string.settings_code_options),
                    onClick = onNavigateToCodeOptions
                )
                SettingsRow(
                    title = stringResource(Res.string.settings_language),
                    subtitle = uiState.languageValue,
                    onClick = { /* TODO */ }
                )
                SettingsRowWithBadge(
                    title = stringResource(Res.string.settings_accounts),
                    count = uiState.accountsCount,
                    onClick = { showAccountsSheet = true }
                )
                SettingsRow(
                    title = stringResource(Res.string.settings_app_lock),
                    onClick = { /* TODO */ }
                )
                SectionDivider()
            }

            // Subscriptions Section
            item {
                SettingsSectionHeader(stringResource(Res.string.settings_section_subscriptions))
                SettingsRow(
                    title = stringResource(Res.string.settings_copilot),
                    subtitle = uiState.copilotTier,
                    onClick = { /* TODO */ }
                )
                SectionDivider()
            }

            // More Options Section
            item {
                SettingsSectionHeader(stringResource(Res.string.settings_section_more_options))
                SettingsRow(title = stringResource(Res.string.settings_share_feedback), onClick = { uriHandler.openUri("https://github.com/mobile/feedback") })
                SettingsRow(title = stringResource(Res.string.settings_get_help), onClick = { uriHandler.openUri("https://support.github.com") })
                SettingsRow(title = stringResource(Res.string.settings_terms_of_service), onClick = { uriHandler.openUri("https://docs.github.com/en/site-policy/github-terms/github-terms-of-service") })
                SettingsRow(title = stringResource(Res.string.settings_privacy_policy), onClick = { uriHandler.openUri("https://docs.github.com/en/site-policy/privacy-policies/github-general-privacy-statement") })
                SettingsRow(title = stringResource(Res.string.settings_open_source_libraries), onClick = { /* TODO */ })
                SettingsRow(
                    title = stringResource(Res.string.settings_sign_out),
                    titleColor = MaterialTheme.colorScheme.error,
                    onClick = { /* TODO */ }
                )
                SectionDivider()
            }

            // Footer
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SettingsTokens.PaddingHuge),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.appVersion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = SettingsTokens.PaddingLarge, end = SettingsTokens.PaddingLarge, top = SettingsTokens.PaddingExtraLarge, bottom = SettingsTokens.PaddingMedium)
    )
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor
            )
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsRowWithBadge(
    title: String,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(start = SettingsTokens.PaddingMedium)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsTokens.DividerThickness)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    )
}
