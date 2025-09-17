package com.liulkovich.notes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NotesDao

//    companion object{
//
//        private var instance: NotesDatabase? = null
//        private val LOCK = Any()
//
//        fun getInstance(context: Context): NotesDatabase {
//
//            instance?.let { return it }
//
//            synchronized(LOCK){
//                instance?.let { return it }
//
//                return Room.databaseBuilder(
//                    context = context,
//                    klass = NotesDatabase::class.java,
//                    name = "notes.db"
//                ).build().also {
//                    instance = it
//                }
//            }
//        }
//    }
}