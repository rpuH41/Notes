package com.liulkovich.notes.data

import android.content.Context
import com.liulkovich.notes.domain.Note
import com.liulkovich.notes.domain.NotesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): NotesRepository {

    private val notesDatabase = NotesDatabase.getInstance(context)
    private val notesDao = notesDatabase.notesDao()

    override suspend fun addNotes(
        title: String,
        content: String,
        isPinned: Boolean,
        updateAt: Long,
    ) {
        val noteDbModel = NoteDbModel(0, title, content, updateAt, isPinned)
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deletedNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.geAllNotes().map { it.toEntities() }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map{ it.toEntities()}
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    companion object {

        private val LOCK = Any()
        private var instance: NotesRepositoryImpl? = null

        fun getInstance(context: Context): NotesRepositoryImpl {

            instance?.let { return it }

            synchronized(LOCK) {
                instance?.let { return it }

                return NotesRepositoryImpl(context).also {
                    instance = it
                }
            }
        }
    }
}