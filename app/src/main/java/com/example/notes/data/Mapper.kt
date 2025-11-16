package com.example.notes.data

import com.example.notes.domain.ContentItem
import com.example.notes.domain.Note
import kotlinx.serialization.json.Json

fun Note.toDBModel(): NoteDBModel {
    val contentAsString = Json.encodeToString(content.toContentItemDbModels())
    return NoteDBModel(id, title, contentAsString, updatedAt, isPinned)
}

fun List<ContentItem>.toContentItemDbModels(): List<ContentItemDBModel> {
    return map { contentItem ->
        when(contentItem) {
            is ContentItem.Image -> {
                ContentItemDBModel.Image(contentItem.url)
            }
            is ContentItem.Text -> {
                ContentItemDBModel.Text(contentItem.content)
            }
        }
    }
}

fun List<ContentItemDBModel>.toContentItems(): List<ContentItem> {
    return map { contentItem ->
        when(contentItem) {
            is ContentItemDBModel.Image -> {
                ContentItem.Image(contentItem.url)
            }
            is ContentItemDBModel.Text -> {
                ContentItem.Text(contentItem.content)
            }
        }
    }
}
fun NoteDBModel.toEntity(): Note {

    val contentItemDBModels = Json.decodeFromString<List<ContentItemDBModel>>(content)
    return Note(id, title, contentItemDBModels.toContentItems(), updatedAt, isPinned)
}

fun List<NoteDBModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}