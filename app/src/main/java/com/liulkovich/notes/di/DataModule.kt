package com.liulkovich.notes.di

import android.content.Context
import androidx.room.Room
import com.liulkovich.notes.data.NotesDao
import com.liulkovich.notes.data.NotesDatabase
import com.liulkovich.notes.data.NotesRepositoryImpl
import com.liulkovich.notes.domain.AddNoteUseCase
import com.liulkovich.notes.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NotesDatabase::class.java,
                name = "notes.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Singleton
        @Provides
        fun provideNotesDao(
            database: NotesDatabase
        ): NotesDao {
            return database.notesDao()
        }

        @Provides
        fun provideAddNoteUseCase(
            repository: NotesRepository
        ): AddNoteUseCase {
            return AddNoteUseCase(repository)
        }
    }
}