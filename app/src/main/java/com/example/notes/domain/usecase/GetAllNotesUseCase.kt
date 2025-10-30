package com.example.notes.domain.usecase

import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}