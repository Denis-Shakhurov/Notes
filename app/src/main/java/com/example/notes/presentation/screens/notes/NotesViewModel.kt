@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.notes.presentation.screens.notes

import androidx.lifecycle.ViewModel
import com.example.notes.data.TestNoteRepositoryImpl
import com.example.notes.domain.Note
import com.example.notes.domain.usecase.AddNoteUseCase
import com.example.notes.domain.usecase.DeleteNoteUseCase
import com.example.notes.domain.usecase.EditNoteUseCase
import com.example.notes.domain.usecase.GetAllNotesUseCase
import com.example.notes.domain.usecase.GetNoteUseCase
import com.example.notes.domain.usecase.SearchNotesUseCase
import com.example.notes.domain.usecase.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class NotesViewModel: ViewModel() {
    private val repository = TestNoteRepositoryImpl

    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val searchNotesUseCase = SearchNotesUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val query = MutableStateFlow("")
    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()

    val scope = CoroutineScope(Dispatchers.IO)
    init {
        addSomeNotes()

        query
            .onEach { input ->
                _state.update { it.copy(query = input) }
            }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNotesUseCase(input)
                }
            }
            .onEach { notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter { !it.isPinned }

                _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
            }
            .launchIn(scope)
    }

    private fun addSomeNotes() {
        repeat(50) {
            addNoteUseCase(title = "Title - $it", content = "Content $it")
        }
    }

    fun processCommand(command: NotesCommand) {
        when (command) {
            is NotesCommand.DeleteNote -> {
                deleteNoteUseCase(command.noteId)
            }
            is NotesCommand.EditNote -> {
                val note = getNoteUseCase(command.note.id)
                val title = note.title

                editNoteUseCase(note.copy(title = "$title - edited"))
            }
            is NotesCommand.InputSearchQuery -> {
                query.update { command.query.trim() }
            }
            is NotesCommand.SwitchPinnedStatus -> {
                switchPinnedStatusUseCase(command.noteId)
            }
        }
    }
}

sealed interface NotesCommand {

    data class InputSearchQuery(val query: String): NotesCommand
    data class SwitchPinnedStatus(val noteId: Int): NotesCommand

    //Temp
    data class DeleteNote(val noteId: Int): NotesCommand
    data class EditNote(val note: Note): NotesCommand
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
)