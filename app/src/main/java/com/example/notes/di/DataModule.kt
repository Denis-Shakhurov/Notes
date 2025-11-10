package com.example.notes.di

import android.content.Context
import com.example.notes.data.NoteRepositoryImpl
import com.example.notes.data.NotesDao
import com.example.notes.data.NotesDatabase
import com.example.notes.domain.repository.NoteRepository
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
    fun bindNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return NotesDatabase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideNoteDao(
            notesDatabase: NotesDatabase
        ): NotesDao {
            return notesDatabase.notesDao()
        }
    }
}