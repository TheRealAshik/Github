package dev.therealashik.github.pullrequest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.base
import github.composeapp.generated.resources.choose_branch_to_compare
import github.composeapp.generated.resources.choose_branches
import github.composeapp.generated.resources.close
import github.composeapp.generated.resources.compare
import github.composeapp.generated.resources.compare_changes
import github.composeapp.generated.resources.main_branch_dropdown
import github.composeapp.generated.resources.select_branch
import org.jetbrains.compose.resources.stringResource
import dev.therealashik.github.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareBranchSheet(
    sheetState: SheetState,
    repoPath: String,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = Dimens.SpacingMedium)
                        .size(width = Dimens.SpacingExtraExtraLarge, height = Dimens.SpacingSmall)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMedium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.close)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = repoPath,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(Dimens.IconSizeLarge)) // To balance the close button
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            Text(
                text = stringResource(Res.string.compare_changes),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))

            Text(
                text = stringResource(Res.string.choose_branches),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.base),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                FilterChip(
                    selected = true,
                    onClick = { /* TODO */ },
                    label = { Text(stringResource(Res.string.main_branch_dropdown)) }
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.compare),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
                TextButton(onClick = { /* TODO */ }) {
                    Text(
                        text = stringResource(Res.string.select_branch),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SpacingExtraExtraLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.IconSizeLarge)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.CallSplit, // Using CallSplit as placeholder
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Dimens.IconSizeMedium)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                    Text(
                        text = stringResource(Res.string.choose_branch_to_compare),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))
        }
    }
}
