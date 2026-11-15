package com.brainpad.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.brainpad.app.ui.screens.NotesListScreen
import com.brainpad.app.viewmodel.NotesView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.brainpad.app.ui.screens.NoteEditScreen
import com.brainpad.app.ui.screens.NoteNotFoundScreen
import com.brainpad.app.ui.theme.BrainPadTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "BrainPad_Lifecycle"
    }

    private val viewModel: NotesView by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        enableEdgeToEdge()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        if (savedInstanceState == null) {
            handleDeepLink(intent)
        }

        setContent {

            val listNotes by viewModel.notes.collectAsState()
            val activeNoteId by viewModel.activeNoteId.collectAsState()
            val isEditing by viewModel.isEditing.collectAsState()
            val notFoundNoteId by viewModel.notFoundNoteId.collectAsState()

            when {
                notFoundNoteId != null -> {
                    BrainPadTheme() {
                        NoteNotFoundScreen(
                            noteId = notFoundNoteId!!,
                            onBackToAllNotesClick = {
                                viewModel.clearNotFoundError()
                            }
                        )
                    }
                }

                isEditing -> {
                    val currentNote = activeNoteId?.let { viewModel.getNoteById(it) }

                    BrainPadTheme() {
                        NoteEditScreen(
                            noteId = activeNoteId ?: "",
                            initialTitle = currentNote?.title.orEmpty(),
                            initialContent = currentNote?.content.orEmpty(),
                            onBackClick = {
                                viewModel.closeNote()
                            },
                            onShareClick = {
                                currentNote?.let { note ->
                                    shareNote(note.id)
                                }
                            }
                        )
                    }
                }

                else -> {
                    BrainPadTheme() {
                        NotesListScreen(
                            notes = listNotes,
                            onNoteClick = { noteId ->
                                viewModel.openNote(noteId)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.d(TAG, "onNewIntent")
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data ?: return

        if (data!!.scheme == "brainpad") {

            if (data.pathSegments.size != 1) {
                showErrorToast(getString(R.string.error_invalid_path))
                return
            }

            val requestedId = data.lastPathSegment


            if (data.host != "note") {
                showErrorToast(getString(R.string.error_invalid_host))
                return
            }

            if (requestedId.isNullOrBlank()) {
                showErrorToast(getString(R.string.error_empty_id))
                return
            }

            val note = viewModel.getNoteById(requestedId)
            if (note != null) {
                viewModel.clearNotFoundError()
                viewModel.openNote(note.id)
            } else {
                viewModel.closeNote()
                viewModel.showNotFoundError(requestedId)
            }
        }
    }

    private fun shareNote(noteId: String) {
        val deepLinkUrl = "brainpad://note/$noteId"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, deepLinkUrl)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_chooser_title))
        startActivity(shareIntent)
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }


}