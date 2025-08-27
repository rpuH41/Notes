package com.liulkovich.notes.domain

import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun addNotes(
        title: String,
        content: String,
        isPinned: Boolean,
        updateAt: Long,
    )

    suspend fun deletedNote(noteId: Int)

    suspend fun editNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNote(noteId: Int): Note

    fun searchNotes(query: String): Flow<List<Note>>

    suspend fun switchPinnedStatus(noteId: Int)
}