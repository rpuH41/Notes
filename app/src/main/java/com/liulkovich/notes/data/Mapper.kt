package com.liulkovich.notes.data

import com.liulkovich.notes.domain.ContentItem
import com.liulkovich.notes.domain.Note

fun Note.toDbModel(): NoteDbModel {

    return NoteDbModel(id, title, updateAt, isPinned)
}

fun List<ContentItem>.toContentItemDbModels(noteId: Int): List<ContentItemDbModel> {
    return mapIndexed { index, contentItem ->

        when(contentItem){
            is ContentItem.Image -> {
                ContentItemDbModel(
                    noteId = noteId,
                    contentType = ContentType.IMAGE,
                    content = contentItem.url,
                    order = index
                )

            }
            is ContentItem.Text -> {
                ContentItemDbModel(
                    noteId = noteId,
                    contentType = ContentType.TEXT,
                    content = contentItem.content,
                    order = index
                )
            }
        }
    }
}

fun List<ContentItemDbModel>.toContentItems(): List<ContentItem> {
    return map { contentItem ->
       when(contentItem.contentType){
           ContentType.TEXT -> {
               ContentItem.Text(content = contentItem.content)
           }
           ContentType.IMAGE -> {
               ContentItem.Image(url = contentItem.content)
           }
       }
    }
}

fun NoteWithContentDbModel.toEntity(): Note {

    return Note(
        id = noteDbModel.id,
        title = noteDbModel.title,
        content = content.toContentItems(),
        updateAt = noteDbModel.updateAt,
        isPinned = noteDbModel.isPinned
    )
}

fun List<NoteWithContentDbModel>.toEntities(): List<Note> {
    return map {it.toEntity()}
}