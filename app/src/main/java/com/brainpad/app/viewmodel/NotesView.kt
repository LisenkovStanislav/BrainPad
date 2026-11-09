package com.brainpad.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainpad.app.model.Note
import com.brainpad.app.model.NotesAction
import com.brainpad.app.model.NotesResult
import com.brainpad.app.model.NotesUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class NotesView(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_ACTIVE_NOTE_ID = "active_note_id"
        private const val KEY_ERROR_NOTE_ID = "error_note_id"
        private const val KEY_SORT_ASC = "sort_asc"
    }

    private val rawNotes = listOf(
        Note(
            id = "949a5444-46bc-4464-894e-289b57dff8da",
            title = "Note 1",
            content = "Lecture 1\nIntroduction to Android\nAndroid is an operating system " +
                    "developed by Google, designed primarily for smartphones and tablets " +
                    "(touchscreen-based mobile devices originally; later for other devices, " +
                    "such as TVs), based on a modified version of the Linux kernel and other " +
                    "open-source software.",
            timestamp = 1710000000000L
        ),
        Note(
            id = "672f8d2b-3651-4e7a-b8c2-34dfb9aad232",
            title = "Note 2",
            content = "Content 2",
            timestamp = 1710086400000L
        )
    )

    private val _uiState = MutableStateFlow(NotesUiState(result = NotesResult.Loading))

    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }


    private fun loadNotes() {
        viewModelScope.launch {
            _uiState.update { it.copy(result = NotesResult.Loading) }
            delay(500.milliseconds)

            val restoredNoteId = savedStateHandle.get<String>(KEY_ACTIVE_NOTE_ID)
            val restoredErrorId = savedStateHandle.get<String>(KEY_ERROR_NOTE_ID)
            val isAsc = savedStateHandle.get<Boolean>(KEY_SORT_ASC) ?: false

            val restoredNote = restoredNoteId?.let { id -> getNoteById(id) }

            _uiState.update { current ->
                current.copy(
                    isAscendingSort = isAsc,
                    activeNote = restoredNote,
                    result = when {
                        restoredErrorId != null -> NotesResult.Error(restoredErrorId)
                        rawNotes.isEmpty() -> NotesResult.Empty
                        else -> NotesResult.Content(sortNotes(rawNotes, isAsc))
                    }
                )
            }
        }
    }


    fun onAction(action: NotesAction) {
        when (action) {
            is NotesAction.OpenNote -> openNote(action.noteId)
            is NotesAction.CloseNote -> closeNote()
            is NotesAction.ToggleSort -> toggleSort()
            is NotesAction.ClearError -> clearNotFoundError()
            is NotesAction.MessageShown -> clearUserMessage()
        }
    }

    fun openNote(id: String) {
        val note = getNoteById(id)
        if (note != null) {
            savedStateHandle[KEY_ERROR_NOTE_ID] = null
            savedStateHandle[KEY_ACTIVE_NOTE_ID] = note.id
            _uiState.update { current ->
                current.copy(
                    activeNote = note,
                    result = if (rawNotes.isEmpty()) NotesResult.Empty
                    else NotesResult.Content(sortNotes(rawNotes, current.isAscendingSort))
                )
            }
        } else {
            showNotFoundError(id)
        }
    }

    fun closeNote() {
        savedStateHandle[KEY_ACTIVE_NOTE_ID] = null
        _uiState.update { it.copy(activeNote = null) }
    }

    fun showNotFoundError(id: String) {
        savedStateHandle[KEY_ACTIVE_NOTE_ID] = null
        savedStateHandle[KEY_ERROR_NOTE_ID] = id
        _uiState.update { current ->
            current.copy(
                activeNote = null,
                result = NotesResult.Error(notFoundId = id)
            )
        }
    }

    fun clearNotFoundError() {
        savedStateHandle[KEY_ERROR_NOTE_ID] = null
        _uiState.update { current ->
            current.copy(
                result = if (rawNotes.isEmpty()) NotesResult.Empty
                else NotesResult.Content(sortNotes(rawNotes, current.isAscendingSort))
            )
        }
    }


    private fun toggleSort() {
        val newSortAsc = !_uiState.value.isAscendingSort
        savedStateHandle[KEY_SORT_ASC] = newSortAsc

        _uiState.update { current ->
            val updatedResult = if (rawNotes.isEmpty()) {
                NotesResult.Empty
            } else {
                NotesResult.Content(sortNotes(rawNotes, newSortAsc))
            }
            current.copy(
                isAscendingSort = newSortAsc,
                result = updatedResult,
                // Add to string.xml
                userMessage = if (newSortAsc) "Sorted: oldest first" else "Sorted: newest first"
            )
        }
    }


    private fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun getNoteById(id: String): Note? {
        return rawNotes.find { it.id == id }
    }

    private fun sortNotes(notes: List<Note>, ascending: Boolean): List<Note> {
        return if (ascending) notes.sortedBy { it.timestamp }
        else notes.sortedByDescending { it.timestamp }
    }
}