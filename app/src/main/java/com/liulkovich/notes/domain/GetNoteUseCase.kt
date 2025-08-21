package com.liulkovich.notes.domain

class GetNoteUseCase(
    private val repository: NotesRepository
) {

    operator fun invoke(noteId: Int) {
        repository.getNote(noteId)
    }
}