@file:Suppress("OPT_IN_USAGE")

package com.liulkovich.notes.pressentation.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liulkovich.notes.data.TestNotesRepositoryImpl
import com.liulkovich.notes.domain.AddNoteUseCase
import com.liulkovich.notes.domain.DeleteNoteUseCase
import com.liulkovich.notes.domain.EditNoteUseCase
import com.liulkovich.notes.domain.GetAllNotesUseCase
import com.liulkovich.notes.domain.GetNoteUseCase
import com.liulkovich.notes.domain.Note
import com.liulkovich.notes.domain.SearchNotesUseCase
import com.liulkovich.notes.domain.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel: ViewModel() {

    private val repository = TestNotesRepositoryImpl

    private val addNoteUseCase = AddNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val searchNotesUseCase = SearchNotesUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()


    init {
        addSomeNotes()
        query
            .onEach {input ->
                _state.update { it.copy(query = input) }
            }
            .flatMapLatest{input ->
                if (input.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNotesUseCase(input)
                }
            }
            .onEach {notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val othersNotes = notes.filter { !it.isPinned }
                _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = othersNotes) }
            }
            .launchIn(viewModelScope)

    }

    //TODO: don't forget to remove it

    private fun addSomeNotes() {
        repeat(10_000) {
            viewModelScope.launch {
                addNoteUseCase(title = "Title №$it", content = "Content №$it")
            }
        }
    }

    fun processCommand(command: NotesCommand) {
        viewModelScope.launch {
            when(command){
                is NotesCommand.DeleteNote -> {
                    deleteNoteUseCase(command.noteId)
                }

                is NotesCommand.EditNote -> {
                    val note = getNoteUseCase(command.note.id)
                    val title = note.title
                    editNoteUseCase(note.copy(title = "$title edited"))
                }

                is NotesCommand.InputSearchQuery -> {
                    query.update { command.query.trim() }
                }

                is NotesCommand.SwitchPinnedStatus -> {
                    switchPinnedStatusUseCase(command.noteId)
                }
            }
        }
    }
}

sealed interface NotesCommand {

    data class InputSearchQuery(val query: String): NotesCommand

    data class SwitchPinnedStatus(val noteId: Int): NotesCommand

    // Temp

    data class DeleteNote(val noteId: Int): NotesCommand

    data class EditNote(val note: Note): NotesCommand
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()

    )
