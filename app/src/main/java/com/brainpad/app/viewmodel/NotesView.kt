package com.brainpad.app.viewmodel

import androidx.lifecycle.ViewModel
import com.brainpad.app.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesView : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(
        listOf(Note(title = "Note 1",
            content = "Lecture 1\nIntroduction to Android\nAndroid is an operating system " +
                    "developed by Google, designed primarily for smartphones and tablets " +
                    "(touchscreen-based mobile devices originally; later for other devices, " +
                    "such as TVs), based on a modified version of the Linux kernel and other " +
                    "open-source software."),
            Note(title = "Note 2", content = "Content 2"))
    )

    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }


}