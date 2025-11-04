package com.example.notes.data

import com.example.notes.domain.Note

fun Note.toDBModel(): NoteDBModel {
    return NoteDBModel(id, title, content, updatedAt, isPinned)
}

fun NoteDBModel.toEntity(): Note {
    return Note(id, title, content, updatedAt, isPinned)
}

fun List<NoteDBModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}