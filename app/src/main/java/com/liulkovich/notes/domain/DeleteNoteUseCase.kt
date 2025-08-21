package com.liulkovich.notes.domain

class DeleteNoteUseCase(
    private val repository: NotesRepository
) {


    operator fun invoke(noteId: Int){
       repository.deletedNote(noteId)
    }
}