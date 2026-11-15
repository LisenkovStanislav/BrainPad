package com.brainpad.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.brainpad.app.R
import com.brainpad.app.ui.theme.AppSizes
import com.brainpad.app.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    noteId: String,
    initialTitle: String,
    initialContent: String,
    onBackClick: () -> Unit,
    onSaveClick: (title: String, content: String) -> Unit
) {
    var title by rememberSaveable(noteId) { mutableStateOf(initialTitle) }
    var content by rememberSaveable(noteId) { mutableStateOf(initialContent) }

    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    val hasUnsavedChanges = title != initialTitle || content != initialContent

    BackHandler(enabled = hasUnsavedChanges) {
        showExitDialog = true
    }

    val handleBackAttempt = {
        if (hasUnsavedChanges) {
            showExitDialog = true
        } else {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (initialTitle.isEmpty() && initialContent.isEmpty())
                            stringResource(R.string.new_note_title)
                        else stringResource(R.string.edit_note_title)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = handleBackAttempt) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onSaveClick(title, content) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.cd_save_note)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppSpacing.screenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text(stringResource(R.string.title_placeholder)) },
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(AppSpacing.screenPadding))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text(stringResource(R.string.content_placeholder)) },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .defaultMinSize(minHeight = AppSizes.editorMinHeight)
            )
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.discard_changes_title)) },
            text = { Text(stringResource(R.string.discard_changes_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onBackClick()
                    }
                ) {
                    Text(stringResource(R.string.discard_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(stringResource(R.string.discard_cancel))
                }
            }
        )
    }
}