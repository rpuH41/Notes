package com.liulkovich.notes.pressentation.screens.creation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liulkovich.notes.data.NotesRepositoryImpl
import com.liulkovich.notes.domain.AddNoteUseCase
import com.liulkovich.notes.domain.ContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
): ViewModel() {

    private val _state = MutableStateFlow<CreateNoteState>(CreateNoteState.Creation())
    val state =_state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when(command) {
            CreateNoteCommand.Back -> {
                _state.update { CreateNoteState.Finished }
            }
            is CreateNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy (
                            content = command.content,
                            isSaveEnabled = previousState.title.isNotBlank() && command.content.isNotBlank()
                        )
                    } else {
                        CreateNoteState.Creation(content = command.content)
                    }
                }
            }
            is CreateNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy (
                            title = command.title,
                            isSaveEnabled = previousState.content.isNotBlank() && command.title.isNotBlank()
                        )
                    } else {
                        CreateNoteState.Creation(title = command.title)
                    }
                }
            }
            CreateNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is CreateNoteState.Creation) {
                            val title = previousState.title
                            val content = ContentItem.Text(content = previousState.content)
                            addNoteUseCase(title, listOf(content))
                            CreateNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }
        }
    }
}

sealed interface CreateNoteCommand {

    data class InputTitle(val title: String): CreateNoteCommand

    data class InputContent(val content: String): CreateNoteCommand

    data object Save: CreateNoteCommand

    data object Back: CreateNoteCommand

}

sealed interface CreateNoteState{

    data class Creation(
        val title: String = "",
        val content: String = "",
        val isSaveEnabled: Boolean = false
    ): CreateNoteState

    data object Finished: CreateNoteState
}