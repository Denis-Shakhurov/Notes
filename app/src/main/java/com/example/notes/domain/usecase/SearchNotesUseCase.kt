package com.example.notes.domain.usecase

import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor (
    private val repository: NoteRepository
) {

    operator fun invoke(query: String): Flow<List<Note>> = repository.searchNotes(query)
}