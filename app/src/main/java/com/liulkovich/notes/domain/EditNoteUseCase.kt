package com.liulkovich.notes.domain

import androidx.compose.animation.core.updateTransition

class EditNoteUseCase(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(note: Note){
       repository.editNote(
           note.copy(updateAt = System.currentTimeMillis())
       )
    }
}