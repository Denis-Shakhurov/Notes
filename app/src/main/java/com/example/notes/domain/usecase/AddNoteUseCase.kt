package com.example.notes.domain.usecase

import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(title: String, content: String) {
        repository.addNote(title, content)
    }
}