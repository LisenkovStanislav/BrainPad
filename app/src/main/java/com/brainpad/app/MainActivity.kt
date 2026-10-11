package com.brainpad.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.brainpad.app.ui.screens.NotesListScreen
import com.brainpad.app.viewmodel.NotesView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.brainpad.app.ui.screens.NoteEditScreen
import com.brainpad.app.ui.theme.BrainPadTheme

class MainActivity : ComponentActivity() {

    private val viewModel: NotesView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val listNotes by viewModel.notes.collectAsState()

            var activeNoteId by rememberSaveable { mutableStateOf<String?>(null) }
            var isEditing by rememberSaveable { mutableStateOf(false) }

            if (isEditing) {
                // Экран создания/редактирования
                val currentNote = activeNoteId?.let { viewModel.getNoteById(it) }

                BrainPadTheme() {
                    NoteEditScreen(
                        initialTitle = currentNote?.title.orEmpty(),
                        initialContent = currentNote?.content.orEmpty(),
                        onBackClick = {
                            isEditing = false
                            activeNoteId = null
                        }
                    )
                }
            } else {
                BrainPadTheme() {
                    NotesListScreen(
                        notes = listNotes,
                        onNoteClick = { noteId ->
                            activeNoteId = noteId
                            isEditing = true
                        }
                    )
                }
            }
        }
    }
}