package com.liulkovich.notes.pressentation.screens.editing

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liulkovich.notes.domain.ContentItem
import com.liulkovich.notes.domain.DeleteNoteUseCase
import com.liulkovich.notes.domain.EditNoteUseCase
import com.liulkovich.notes.domain.GetNoteUseCase
import com.liulkovich.notes.domain.Note
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditNoteViewModel.Factory::class)
class EditNoteViewModel @AssistedInject constructor(

    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deletedNoteUseCase: DeleteNoteUseCase,
    @Assisted("noteId") private val noteId: Int,

): ViewModel() {

    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)
    val state =_state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                val content = if (note.content.lastOrNull() !is ContentItem.Text){
                    note.content + ContentItem.Text("")
                } else {
                    note.content
                }
                EditNoteState.Editing(note.copy(content = content))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("noteId") noteId: Int
        ): EditNoteViewModel
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            is EditNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val newContent = previousState
                            .note
                            .content
                            .mapIndexed { index, contentItem ->
                                if (index == command.index && contentItem is ContentItem.Text) {
                                    contentItem.copy(content = command.content)
                                } else {
                                    contentItem
                                }
                            }
                        val newNote = previousState.note.copy(content = newContent)
                        previousState.copy(
                            note = newNote
                        )
                    } else {
                        previousState
                    }
                }
            }

            is EditNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val newNote = previousState.note.copy(title = command.title)
                        previousState.copy(note = newNote)
                    } else {
                        previousState
                    }
                }
            }

            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Editing) {
                            val note = previousState.note
                            editNoteUseCase(note)
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Editing) {
                            val note = previousState.note
                            deletedNoteUseCase(note.id)
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            is EditNoteCommand.AddImage -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val oldNote = previousState.note
                        oldNote.content.toMutableList().apply {
                            val lastItem = last()
                            if (lastItem is ContentItem.Text && lastItem.content.isBlank()) {
                                removeAt(lastIndex)
                            }
                            add(ContentItem.Image(command.uri.toString()))
                            add(ContentItem.Text(""))
                        }.let {
                            val newNote = oldNote.copy(content = it)
                            previousState.copy(note = newNote)
                        }
                    } else {
                        previousState
                    }
                }
            }

            is EditNoteCommand.DeleteImage -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val oldNote = previousState.note
                        oldNote.content.toMutableList().apply {
                            removeAt(command.index)
                        }.let {
                            val newNote = oldNote.copy(content = it)
                            previousState.copy(note = newNote)
                        }
                    } else {
                        previousState
                    }
                }
            }
        }
    }
}

sealed interface EditNoteCommand {

    data class InputTitle(val title: String): EditNoteCommand

    data class InputContent(val content: String, val index: Int): EditNoteCommand

    data class AddImage(val uri: Uri) : EditNoteCommand

    data class DeleteImage(val index: Int) : EditNoteCommand

    data object Save: EditNoteCommand

    data object Back: EditNoteCommand

    data object Delete: EditNoteCommand

}

sealed interface EditNoteState {

    data object Initial : EditNoteState

    data class Editing(
        val note: Note
    ) : EditNoteState {

        val isSaveEnabled: Boolean
            get() {
                return when {
                    note.title.isBlank() -> false
                    note.content.isEmpty() -> false
                    else -> {
                        note.content.any {
                            it !is ContentItem.Text || it.content.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished : EditNoteState
}
