package com.el3asas.note.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.el3asas.note.data.models.NoteEntity

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity) : NoteEntity

    @Query("select * from notes")
    suspend fun getNotes() : List<NoteEntity>

}