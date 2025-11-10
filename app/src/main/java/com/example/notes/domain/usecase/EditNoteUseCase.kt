package com.example.notes.domain.usecase

import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository
import javax.inject.Inject

class EditNoteUseCase @Inject constructor (
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.editNote(
            note.copy(
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}