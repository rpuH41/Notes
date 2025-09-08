package com.liulkovich.notes.data

import com.liulkovich.notes.domain.Note

fun Note.toDbModel(): NoteDbModel {
    return NoteDbModel(id, title, content, updateAt, isPinned)
}

fun NoteDbModel.toEntity(): Note {
    return Note(id, title, content, updateAt, isPinned)
}

fun List<NoteDbModel>.toEntities(): List<Note> {
    return map {it.toEntity()}
}