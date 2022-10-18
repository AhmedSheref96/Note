package com.el3asas.note.repositories.home

import androidx.lifecycle.LiveData
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.data.source.local.daos.NoteDao
import timber.log.Timber
import javax.inject.Inject

class NotesRepository @Inject constructor(private val noteDao: NoteDao) {
    fun getHomeNotes() : LiveData<List<NoteEntity>?> = noteDao.getNotes()

    suspend fun addNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun update(note: NoteEntity) {
        TODO("Not yet implemented")
    }

}