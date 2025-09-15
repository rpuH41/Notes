package com.liulkovich.notes.di

import android.content.Context
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
interface NotesModule {

    @Singleton
    @Binds
    fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object {
        @Singleton
        @Provides
        fun provideNotesRepository(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return NotesDatabase.getInstance(context)
        }

        @Provides
        fun provideAddNoteUseCase(
            repository: NotesRepository
        ): AddNoteUseCase {
            return AddNoteUseCase(repository)
        }
    }
}