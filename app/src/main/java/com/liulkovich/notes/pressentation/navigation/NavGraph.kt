package com.liulkovich.notes.pressentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.liulkovich.notes.pressentation.screens.creation.CreateNoteScreen
import com.liulkovich.notes.pressentation.screens.editing.EditNoteScreen
import com.liulkovich.notes.pressentation.screens.notes.NotesScreen

@Composable
fun NavGraph() {
    val screen = remember {
        mutableStateOf<Screen>(Screen.Notes)
    }
    when(val currentScreen = screen.value) {
        Screen.CreateNote -> {
            CreateNoteScreen(
                onFinished = {
                    screen.value = Screen.Notes
                }
            )
        }
        is Screen.EditNote -> {
            EditNoteScreen(
                noteId = currentScreen.noteId,
                onFinished = {
                    screen.value = Screen.Notes
                }
            )
        }
        Screen.Notes -> {
            NotesScreen(
                onNoteClick = {
                    screen.value = Screen.EditNote(it.id)
                },
                onAddNoteClick = {
                    screen.value = Screen.CreateNote
                }
            )
        }
    }
}

sealed interface Screen {

    data object Notes: Screen

    data object CreateNote: Screen

    data class EditNote(val noteId: Int): Screen
}