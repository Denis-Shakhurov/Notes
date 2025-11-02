package com.example.notes.domain.usecase

import com.example.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}