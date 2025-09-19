package com.liulkovich.notes.data

import com.liulkovich.notes.domain.ContentItem
import com.liulkovich.notes.domain.Note
import com.liulkovich.notes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val imageFileManager: ImageFileManager
): NotesRepository {

    override suspend fun addNotes(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updateAt: Long,
    ) {
        val note = Note(0, title, content.processForStorage(), updateAt, isPinned)
        val noteDbModel = note.toDbModel()
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deletedNote(noteId: Int) {
        val note = notesDao.getNote(noteId).toEntity()
        notesDao.deleteNote(noteId)

        note.content
            .filterIsInstance<ContentItem.Image>()
            .map {it.url}
            .forEach {
                imageFileManager.deleteImage(it)
            }
    }

    override suspend fun editNote(note: Note) {
        val oldNote = notesDao.getNote(note.id).toEntity()

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map{ it.url }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map{ it.url }
        val removedUrls = oldUrls - newUrls

        removedUrls.forEach {
            imageFileManager.deleteImage(it)
        }

        val proсessedContent = note.content.processForStorage()
        val processedNote = note.copy(content = proсessedContent)

        notesDao.addNote(processedNote.toDbModel())
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

    private suspend fun  List<ContentItem>.processForStorage(): List<ContentItem> {
        return map { contentItem ->
            when(contentItem) {
                is ContentItem.Image -> {
                    if(imageFileManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        val internalPath = imageFileManager.copyImageToInternalStorage(contentItem.url)
                        ContentItem.Image(internalPath)
                    }
                }
                is ContentItem.Text -> contentItem
            }
        }
    }
}