package com.example.notes.domain.usecase

import com.example.notes.domain.repository.NoteRepository

class SwitchPinnedStatusUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}