package dev.therealashik.github.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import github.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

data class AccountItem(
    val username: String,
    val notificationCount: Int,
    val isActive: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsSheet(
    accounts: List<AccountItem>,
    onDismiss: () -> Unit,
    onAddAccount: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SettingsTokens.PaddingLarge)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.content_description_close)
                        )
                    }
                    Spacer(modifier = Modifier.width(SettingsTokens.PaddingMedium))
                    Text(
                        text = stringResource(Res.string.accounts_sheet_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                TextButton(onClick = { /* TODO */ }) {
                    Text(
                        text = stringResource(Res.string.accounts_sheet_edit),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Divider()

            // Accounts List
            LazyColumn {
                items(accounts) { account ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO */ }
                            .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingLarge),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar Placeholder
                        Box(
                            modifier = Modifier
                                .size(SettingsTokens.IconSizeLarge)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(SettingsTokens.PaddingLarge))

                        Text(
                            text = account.username,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        if (account.notificationCount > 0) {
                            // Notification Badge
                            Surface(
                                shape = MaterialTheme.shapes.extraLarge,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = SettingsTokens.PaddingMedium)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = SettingsTokens.PaddingMedium, vertical = SettingsTokens.PaddingSmall),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onError,
                                        modifier = Modifier.size(SettingsTokens.IconSizeSmall)
                                    )
                                    Spacer(modifier = Modifier.width(SettingsTokens.PaddingSmall))
                                    Text(
                                        text = account.notificationCount.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onError
                                    )
                                }
                            }
                        }

                        if (account.isActive) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(Res.string.content_description_active_account),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                item {
                    // Add Account Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddAccount() }
                            .padding(horizontal = SettingsTokens.PaddingLarge, vertical = SettingsTokens.PaddingLarge),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.accounts_sheet_add_account),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
