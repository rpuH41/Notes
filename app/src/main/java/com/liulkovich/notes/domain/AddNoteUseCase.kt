package com.liulkovich.notes.domain

class AddNoteUseCase(
    private val repository: NotesRepository
) {

    operator fun invoke(
        title: String,
        content: String
    ) {
        repository.addNotes(title, content)
    }
}