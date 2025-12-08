package com.example.notes.data

import com.example.notes.domain.ContentItem
import com.example.notes.domain.Note
import kotlinx.serialization.json.Json

fun Note.toDBModel(): NoteDBModel {
    return NoteDBModel(id, title, updatedAt, isPinned)
}

fun List<ContentItem>.toContentItemDbModels(noteId: Int): List<ContentItemDBModel> {
    return mapIndexed { index, contentItem ->
        when(contentItem) {
            is ContentItem.Image -> {
                ContentItemDBModel(
                    noteId = noteId,
                    contentType = ContentType.IMAGE,
                    content = contentItem.url,
                    order = index
                )
            }
            is ContentItem.Text -> {
                ContentItemDBModel(
                    noteId = noteId,
                    contentType = ContentType.TEXT,
                    content = contentItem.content,
                    order = index
                )
            }
        }
    }
}

fun List<ContentItemDBModel>.toContentItems(): List<ContentItem> {
    return map { contentItem ->
        when(contentItem.contentType) {
            ContentType.TEXT -> {
                ContentItem.Text(content = contentItem.content)
            }
            ContentType.IMAGE -> {
                ContentItem.Image(url = contentItem.content)
            }
        }
    }
}
fun NoteWithContentDBModel.toEntity(): Note {

    return Note(
        id = noteDBModel.id,
        title = noteDBModel.title,
        content = content.toContentItems(),
        updatedAt = noteDBModel.updatedAt,
        isPinned = noteDBModel.isPinned
    )
}

fun List<NoteWithContentDBModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}