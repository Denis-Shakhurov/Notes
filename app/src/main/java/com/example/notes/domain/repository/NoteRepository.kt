package com.example.notes.domain.repository

import com.example.notes.domain.ContentItem
import com.example.notes.domain.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updatedAt: Long
    )

    suspend fun deleteNote(noteId: Int)

    suspend fun editNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNote(noteId: Int): Note

    fun searchNotes(query: String): Flow<List<Note>>

    suspend fun switchPinnedStatus(noteId: Int)
}