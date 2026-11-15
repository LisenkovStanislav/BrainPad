package com.brainpad.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.brainpad.app.R
import com.brainpad.app.model.*
import com.brainpad.app.ui.theme.*
import com.brainpad.app.viewmodel.NotesView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID


fun NavBackStack<NavKey>.selectNote(id: String) {
    while (size > 1 && (last() is NoteDetailKey || last() is NoteEditKey)) {
        removeLastOrNull()
    }
    add(NoteDetailKey(id))
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NotesScreenContainer(
    viewModel: NotesView,
    deepLinkNoteId: String? = null,
    onDeepLinkConsumed: () -> Unit = {},
    onShareNote: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val backStack = rememberNavBackStack(NoteListKey)

    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive = calculatePaneScaffoldDirective(adaptiveInfo).copy(
        horizontalPartitionSpacerSize = AppSizes.borderThin
    )
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = directive,
        backNavigationBehavior = BackNavigationBehavior.PopLatest
    )

    LaunchedEffect(deepLinkNoteId) {
        deepLinkNoteId?.let { targetId ->
            backStack.selectNote(targetId)
            onDeepLinkConsumed()
        }
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onAction(NotesAction.MessageShown)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(listDetailStrategy),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<NoteListKey>(
                    metadata = ListDetailSceneStrategy.listPane(
                        sceneKey = "notes_scene",
                        detailPlaceholder = {
                            DetailPlaceholderView()
                        }
                    )
                ) {
                    NotesListScreen(
                        state = uiState,
                        onAction = { action ->
                            when (action) {
                                is NotesAction.OpenNote -> backStack.selectNote(action.noteId)
                                else -> viewModel.onAction(action)
                            }
                        },
                        onCreateNoteClick = {
                            val newId = UUID.randomUUID().toString()
                            backStack.add(NoteEditKey(newId))
                        },
                        snackbarHostState = snackbarHostState
                    )
                }

                entry<NoteDetailKey>(
                    metadata = ListDetailSceneStrategy.detailPane(sceneKey = "notes_scene")
                ) { route ->
                    val currentNote = (uiState.result as? NotesResult.Content)
                        ?.notes?.find { it.id == route.noteId }
                        ?: viewModel.getNoteById(route.noteId)

                    NoteDetailScreen(
                        noteId = route.noteId,
                        note = currentNote,
                        onBackClick = { backStack.removeLastOrNull() },
                        onEditClick = { id -> backStack.add(NoteEditKey(id)) },
                        onShareClick = onShareNote
                    )
                }

                entry<NoteEditKey> { route ->
                    val existingNote = (uiState.result as? NotesResult.Content)
                        ?.notes?.find { it.id == route.noteId }
                        ?: viewModel.getNoteById(route.noteId)

                    NoteEditScreen(
                        noteId = route.noteId,
                        initialTitle = existingNote?.title.orEmpty(),
                        initialContent = existingNote?.content.orEmpty(),
                        onBackClick = { backStack.removeLastOrNull() },
                        onSaveClick = { newTitle, newContent ->
                            viewModel.onAction(
                                NotesAction.SaveNote(
                                    noteId = route.noteId,
                                    title = newTitle,
                                    content = newContent
                                )
                            )
                            backStack.removeLastOrNull()
                            if (backStack.lastOrNull() !is NoteDetailKey) {
                                backStack.selectNote(route.noteId)
                            }
                        }
                    )
                }
            }
        )
    }
}


@Composable
fun DetailPlaceholderView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = AppTokens.alphaNotebookLine),
                modifier = Modifier.size(AppSizes.errorIconCircleSize)
            )
            Text(
                text = stringResource(R.string.detail_placeholder),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    state: NotesUiState,
    onAction: (NotesAction) -> Unit,
    onCreateNoteClick: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val isLoading = state.result is NotesResult.Loading

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
                    IconButton(onClick = { /* Поиск */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.cd_search)
                        )
                    }
                    IconButton(onClick = { onAction(NotesAction.ToggleSort) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = stringResource(R.string.cd_sort)
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
                        onClick = { /* Запись голоса */ },
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
                        onClick = onCreateNoteClick,
                        modifier = Modifier.size(AppSizes.fabPrimarySize),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
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
        Box(modifier = Modifier.fillMaxSize()) {
            when (val result = state.result) {
                is NotesResult.Loading -> {
                    LoadingNotesView(modifier = Modifier.fillMaxSize())
                }

                is NotesResult.Empty -> {
                    EmptyNotesContent(
                        onCreateNoteClick = onCreateNoteClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is NotesResult.Error -> {
                    NoteNotFoundContent(
                        noteId = result.notFoundId,
                        onBackToAllNotesClick = { onAction(NotesAction.ClearError) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is NotesResult.Content -> {
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
                        items(
                            count = result.notes.size,
                            key = { index -> result.notes[index].id }
                        ) { index ->
                            val note = result.notes[index]
                            NoteCard(
                                note = note,
                                onClick = { onAction(NotesAction.OpenNote(note.id)) }
                            )
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = AppSpacing.sectionSpacing)
            ) { data ->
                Surface(
                    shape = AppShapes.pillButton,
                    color = Color(0xFF2B2B2E),
                    contentColor = Color.White,
                    shadowElevation = AppSizes.strokeMedium
                ) {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            horizontal = AppSpacing.cardPadding,
                            vertical = AppSpacing.l
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyNotesContent(
    onCreateNoteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = AppSpacing.emptyScreenHorizontal),
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
            onClick = onCreateNoteClick,
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

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locales[0]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, role = Role.Button)
            .semantics(mergeDescendants = true) {},
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
                        text = SimpleDateFormat("MMM d", locale).format(Date(note.timestamp)),
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


@Preview(showBackground = true, name = "1. Content State")
@Composable
private fun NotesListScreenContentPreview() {
    BrainPadTheme(dynamicColor = false) {
        NotesListScreen(
            state = NotesUiState(
                result = NotesResult.Content(
                    notes = listOf(
                        Note(id = "1", title = "Design sync", content = "Simplify onboarding to three steps."),
                        Note(id = "2", title = "Weekend reset", content = "Pick up coffee beans, repot the monstera.")
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "2. Empty State")
@Composable
private fun NotesListScreenEmptyPreview() {
    BrainPadTheme(dynamicColor = false) {
        NotesListScreen(
            state = NotesUiState(result = NotesResult.Empty),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "3. Error State")
@Composable
private fun NotesListScreenErrorPreview() {
    BrainPadTheme(dynamicColor = false) {
        NotesListScreen(
            state = NotesUiState(result = NotesResult.Error(notFoundId = "999")),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "4. Loading State")
@Composable
private fun NotesListScreenLoadingPreview() {
    BrainPadTheme(dynamicColor = false) {
        NotesListScreen(
            state = NotesUiState(result = NotesResult.Loading),
            onAction = {}
        )
    }
}