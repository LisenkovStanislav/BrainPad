package com.brainpad.app.model

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object NoteListKey : NavKey


@Serializable
data class NoteDetailKey(val noteId: String) : NavKey


@Serializable
data class NoteEditKey(val noteId: String) : NavKey