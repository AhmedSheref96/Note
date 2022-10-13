package com.el3asas.note.repositories.home

import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.data.source.local.daos.NoteDao
import javax.inject.Inject

class NotesRepository @Inject constructor(private val noteDao: NoteDao) {
    suspend fun getHomeNotes() : List<NoteEntity> = listOf(NoteEntity(0,"test","test", date = "30/3/2022", images = null))

    suspend fun addNote(note: NoteEntity) {
        TODO("Not yet implemented")
    }

    suspend fun update(note: NoteEntity) {
        TODO("Not yet implemented")
    }

}