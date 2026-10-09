package com.brainpad.app.viewmodel

import androidx.lifecycle.ViewModel
import com.brainpad.app.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesView : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(
        listOf(Note(title = "Note 1", content = "Content 1"),
            Note(title = "Note 2", content = "Content 2"))
    )

    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }


}