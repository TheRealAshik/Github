package dev.therealashik.github.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeOptionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: CodeOptionsViewModel = viewModel { CodeOptionsViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.code_options_title),
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
            item {
                SettingsToggleRow(
                    title = stringResource(Res.string.code_options_scrollable_file_path),
                    checked = uiState.scrollableFilePath,
                    onCheckedChange = { viewModel.toggleScrollableFilePath() }
                )
                SectionDivider()

                SettingsToggleRow(
                    title = stringResource(Res.string.code_options_show_line_numbers),
                    checked = uiState.showLineNumbers,
                    onCheckedChange = { viewModel.toggleShowLineNumbers() }
                )
                SectionDivider()

                SettingsToggleRow(
                    title = stringResource(Res.string.code_options_always_dark_theme),
                    checked = uiState.alwaysUseDarkTheme,
                    onCheckedChange = { viewModel.toggleAlwaysUseDarkTheme() }
                )
                SectionDivider()

                SettingsToggleRow(
                    title = stringResource(Res.string.code_options_override_font_size),
                    checked = uiState.overrideSystemFontSize,
                    onCheckedChange = { viewModel.toggleOverrideSystemFontSize() }
                )
                SectionDivider()

                SettingsToggleRow(
                    title = stringResource(Res.string.code_options_wrap_lines),
                    checked = uiState.wrapLines,
                    onCheckedChange = { viewModel.toggleWrapLines() }
                )
                SectionDivider()
            }

            // Preview Section
            item {
                SettingsSectionHeader(stringResource(Res.string.code_options_preview))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SettingsTokens.PaddingLarge),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = SettingsTokens.PaddingMedium, vertical = SettingsTokens.PaddingSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(Res.string.content_description_collapse),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(Res.string.code_preview_file_path),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f).padding(horizontal = SettingsTokens.PaddingMedium)
                            )
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = stringResource(Res.string.content_description_copy),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = stringResource(Res.string.content_description_more),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Code block
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.scrim)
                                .padding(SettingsTokens.PaddingLarge)
                        ) {
                            val codeColorKeyword = MaterialTheme.colorScheme.primary
                            val codeColorString = MaterialTheme.colorScheme.tertiary
                            val codeColorNormal = MaterialTheme.colorScheme.onSurface

                            val funStr = stringResource(Res.string.code_preview_fun)
                            val mainStr = stringResource(Res.string.code_preview_main)
                            val printlnStr = stringResource(Res.string.code_preview_println)
                            val helloWorldStr = stringResource(Res.string.code_preview_hello_world)
                            val endStr = stringResource(Res.string.code_preview_end)

                            val annotatedCode = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = codeColorKeyword)) {
                                    append(funStr)
                                }
                                withStyle(style = SpanStyle(color = codeColorNormal)) {
                                    append(mainStr)
                                }
                                withStyle(style = SpanStyle(color = codeColorNormal)) {
                                    append(printlnStr)
                                }
                                withStyle(style = SpanStyle(color = codeColorString)) {
                                    append(helloWorldStr)
                                }
                                withStyle(style = SpanStyle(color = codeColorNormal)) {
                                    append(endStr)
                                }
                            }

                            if (uiState.showLineNumbers) {
                                Row {
                                    Column(
                                        modifier = Modifier.padding(end = SettingsTokens.PaddingLarge),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text("1", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                                        Text("2", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                                        Text("3", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                                    }
                                    Text(
                                        text = annotatedCode,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            } else {
                                Text(
                                    text = annotatedCode,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
