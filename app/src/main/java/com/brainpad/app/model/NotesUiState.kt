package com.brainpad.app.model


data class NotesUiState(
    val result: NotesResult = NotesResult.Loading,
    val activeNote: Note? = null,
    val isAscendingSort: Boolean = false,
    val userMessage: String? = null
)


sealed interface NotesResult {
    data object Loading : NotesResult
    data object Empty : NotesResult
    data class Content(val notes: List<Note>) : NotesResult
    data class Error(val notFoundId: String) : NotesResult
}


sealed interface NotesAction {
    data class OpenNote(val noteId: String) : NotesAction
    data object CloseNote : NotesAction
    data object ToggleSort : NotesAction
    data object ClearError : NotesAction
    data object MessageShown : NotesAction
}