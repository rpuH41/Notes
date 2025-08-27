package com.liulkovich.notes.domain

class AddNoteUseCase(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(
        title: String,
        content: String
    ) {
        repository.addNotes(
            title = title,
            content = content,
            isPinned = false,
            updateAt = System.currentTimeMillis()
            )
    }
}