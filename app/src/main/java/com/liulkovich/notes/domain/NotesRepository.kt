package com.liulkovich.notes.domain

import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun addNotes(note: Note)

    fun deletedNote(noteId: Int)

    fun editNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    fun getNote(noteId: Int): Note

    fun searchNotes(query: String): Flow<List<Note>>

    fun switchPinnedStatus(noteId: Int)
}