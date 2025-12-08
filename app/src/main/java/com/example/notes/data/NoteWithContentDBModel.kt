package com.example.notes.data

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithContentDBModel(
    @Embedded
    val noteDBModel: NoteDBModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val content: List<ContentItemDBModel>
)