package com.example.notes.domain.repository

import com.example.notes.domain.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun addNote(note: Note)

    fun deleteNote(noteId: Int)

    fun editNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    fun getNote(noteId: Int): Note

    fun searchNotes(query: String): Flow<List<Note>>

    fun switchPinnedStatus(noteId: Int)
}