package com.example.notes.domain.usecase

import com.example.notes.domain.ContentItem
import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor (
    private val repository: NoteRepository
) {

    suspend operator fun invoke(
        title: String,
        content: List<ContentItem>
    ) {
        repository.addNote(
            title = title,
            content = content,
            isPinned = false,
            updatedAt = System.currentTimeMillis())
    }
}