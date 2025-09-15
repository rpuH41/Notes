package com.liulkovich.notes.domain

import javax.inject.Inject


class EditNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(note: Note){
       repository.editNote(
           note.copy(updateAt = System.currentTimeMillis())
       )
    }
}