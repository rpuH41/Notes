package com.liulkovich.notes.domain

import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(
        title: String,
        content: List<ContentItem>
    ) {
        repository.addNotes(
            title = title,
            content = content,
            isPinned = false,
            updateAt = System.currentTimeMillis()
            )
    }
}