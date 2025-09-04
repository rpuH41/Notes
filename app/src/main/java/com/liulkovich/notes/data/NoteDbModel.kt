package com.liulkovich.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val content: String,
    val updateAt: Long,
    val isPinned: Boolean
)
