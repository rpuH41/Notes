package com.liulkovich.notes.domain

import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {


    suspend operator fun invoke(noteId: Int){
       repository.deletedNote(noteId)
    }
}