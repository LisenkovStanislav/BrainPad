package com.brainpad.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.brainpad.app.R
import com.brainpad.app.model.Note
import com.brainpad.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    notes: List<Note>,
    onNoteClick: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500L)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.cd_search)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.cd_settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (!isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xl)
                ) {
                    SmallFloatingActionButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(AppSizes.fabSmallSize),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = AppShapes.fabSmall
                    ) {
                        Icon(
                            imageVector = Icons.Default.MicNone,
                            contentDescription = stringResource(R.string.cd_record_voice)
                        )
                    }
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(AppSizes.fabPrimarySize),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = AppShapes.fabPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.cd_add_note)
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when {
            isLoading -> {
                LoadingNotesView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            notes.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = AppSpacing.emptyScreenHorizontal),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(AppSizes.illustrationBox),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(
                                    width = AppSizes.illustrationBgWidth,
                                    height = AppSizes.illustrationBgHeight
                                )
                                .clip(shape = AppShapes.illustrationBg)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        )

                        Surface(
                            modifier = Modifier.size(
                                width = AppSizes.notebookWidth,
                                height = AppSizes.notebookHeight
                            ),
                            shape = AppShapes.notebook,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shadowElevation = AppSizes.elevationSmall
                        ) {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .padding(start = AppSpacing.m)
                                        .width(AppSizes.notebookSpineWidth)
                                        .fillMaxHeight()
                                        .alpha(AppTokens.alphaSpine)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            top = AppSpacing.notebookTopPadding,
                                            start = AppSpacing.xxl,
                                            end = AppSpacing.xl
                                        ),
                                    verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(bottom = AppSpacing.xs)
                                            .fillMaxWidth(AppTokens.fractionLineMedium)
                                            .height(AppSizes.notebookLineThick)
                                            .clip(AppShapes.lineMedium)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(AppTokens.fractionLineWide)
                                            .height(AppSizes.notebookLineThin)
                                            .clip(AppShapes.lineSmall)
                                            .alpha(AppTokens.alphaNotebookLine)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(AppTokens.fractionLineShort)
                                            .height(AppSizes.notebookLineThin)
                                            .clip(AppShapes.lineSmall)
                                            .alpha(AppTokens.alphaNotebookLine)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(
                                    x = AppSizes.audioBadgeOffsetX,
                                    y = AppSizes.audioBadgeOffsetY
                                )
                                .size(AppSizes.audioBadgeSize)
                                .clip(AppShapes.audioBadge)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(AppSizes.iconMedium)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))
                    Text(
                        text = stringResource(R.string.no_notes_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xl))
                    Text(
                        text = stringResource(R.string.no_notes_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))
                    Button(
                        onClick = { },
                        modifier = Modifier.height(AppSizes.touchTargetMin),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = AppShapes.pillButton
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(AppSizes.iconSmall)
                        )
                        Spacer(modifier = Modifier.width(AppSpacing.m))
                        Text(
                            text = stringResource(R.string.create_note),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(
                        horizontal = AppSpacing.screenPadding,
                        vertical = AppSpacing.xl
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xl)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.your_notes),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            IconButton(onClick = { /* сортировка */ }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = stringResource(R.string.cd_sort),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    items(
                        count = notes.size,
                        key = { index -> notes[index].id }
                    ) { index ->
                        NoteCard(
                            note = notes[index],
                            onClick = { onNoteClick(notes[index].id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = AppShapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.xl, horizontal = AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (note.title.isNotBlank()) note.title else stringResource(R.string.no_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(AppSpacing.m))

                Surface(
                    shape = AppShapes.dateBadge,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                ) {
                    Text(
                        text = SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(note.timestamp)),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            horizontal = AppSpacing.m,
                            vertical = AppSpacing.xs
                        )
                    )
                }
            }

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}