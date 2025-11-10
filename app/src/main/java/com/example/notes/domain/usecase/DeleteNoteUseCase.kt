package com.example.notes.domain.usecase

import com.example.notes.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor (
    private val repository: NoteRepository
) {

    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}