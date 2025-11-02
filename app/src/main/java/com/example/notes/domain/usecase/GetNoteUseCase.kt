package com.example.notes.domain.usecase

import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(noteId: Int): Note = repository.getNote(noteId)
}