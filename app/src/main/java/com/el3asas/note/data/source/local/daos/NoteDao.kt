package com.el3asas.note.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.el3asas.note.data.models.NoteEntity

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
    fun getNotes(): LiveData<List<NoteEntity>?>

}