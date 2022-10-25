package com.el3asas.note.data.source.local.daos

import androidx.room.*
import com.el3asas.note.data.models.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity): Long

    @Delete(entity = NoteEntity::class)
    suspend fun deleteNote(note: NoteEntity)

    @Query("delete from notes where id=:noteId")
    suspend fun deleteNote(noteId: Long)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Query("select * from notes")
    fun getNotes(): Flow<List<NoteEntity>?>

    @Query("select * from notes where id=:id")
    suspend fun getNoteById(id: Long): NoteEntity?

}