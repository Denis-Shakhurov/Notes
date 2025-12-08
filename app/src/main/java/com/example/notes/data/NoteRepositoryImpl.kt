package com.example.notes.data

import com.example.notes.domain.ContentItem
import com.example.notes.domain.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val imageFileManager: ImageFileManager
) : NoteRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val processContent = content.processForStorage()
        val noteDBModel = NoteDBModel(0, title, updatedAt, isPinned)
        val noteId = notesDao.addNote(noteDBModel).toInt()
        val contentItems = processContent.toContentItemDbModels(noteId)
        notesDao.addNoteContent(contentItems)
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = notesDao.getNote(noteId).toEntity()
        notesDao.deleteNote(noteId)

        note.content
            .filterIsInstance<ContentItem.Image>()
            .map { it.url }
            .forEach {
                imageFileManager.deleteImageInInternalStorage(it)
            }
    }

    override suspend fun editNote(note: Note) {
        val oldNote = notesDao.getNote(note.id).toEntity()

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map { it.url }

        val removeUrls = oldUrls - newUrls

        removeUrls.forEach {
            imageFileManager.deleteImageInInternalStorage(it)
        }

        val processContent = note.content.processForStorage()
        val processNote = note.copy(content = processContent)

        notesDao.addNote(processNote.toDBModel())
        notesDao.deleteNoteContent(note.id)
        notesDao.addNoteContent(
            processContent.toContentItemDbModels(
                note.id
            )
        )
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map { it.toEntities() }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    private suspend fun List<ContentItem>.processForStorage() : List<ContentItem> {
        return map { contentItem ->
            when(contentItem) {
                is ContentItem.Image -> {
                    if (imageFileManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        val internalPath = imageFileManager.copeImageToInternalStorage(contentItem.url)
                        ContentItem.Image(internalPath)
                    }
                }
                is ContentItem.Text -> contentItem
            }
        }
    }
}