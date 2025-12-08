package com.example.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.notes.domain.ContentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Transaction
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteWithContentDBModel>>

    @Transaction
    @Query("""
        SELECT DISTINCT notes.* FROM notes
        JOIN content
        ON notes.id == content.noteId
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%' 
        ORDER BY updatedAt DESC
    """)
    fun searchNotes(query: String): Flow<List<NoteWithContentDBModel>>

    @Transaction
    @Query("DELETE FROM notes WHERE id == :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("UPDATE notes SET isPinned = NOT isPinned WHERE id == :noteId")
    suspend fun switchPinnedStatus(noteId: Int)

    @Insert(onConflict = REPLACE)
    suspend fun addNote(noteDBModel: NoteDBModel) : Long

    @Insert(onConflict = REPLACE)
    suspend fun addNoteContent(content: List<ContentItemDBModel>)

    @Query("DELETE FROM content WHERE noteId == :noteId")
    suspend fun deleteNoteContent(noteId: Int)

    @Query("SELECT * FROM notes WHERE id == :noteId")
    suspend fun getNote(noteId: Int): NoteWithContentDBModel

    @Transaction
    suspend fun addNoteWithContent(
        noteDBModel: NoteDBModel,
        content: List<ContentItem>
    ) {
        val noteId = addNote(noteDBModel).toInt()
        val contentItems = content.toContentItemDbModels(noteId)
        addNoteContent(contentItems)
    }

    @Transaction
    suspend fun updateNote(
        noteDBModel: NoteDBModel,
        content: List<ContentItemDBModel>
    ) {
        addNote(noteDBModel)
        deleteNoteContent(noteDBModel.id)
        addNoteContent(content)
    }
}