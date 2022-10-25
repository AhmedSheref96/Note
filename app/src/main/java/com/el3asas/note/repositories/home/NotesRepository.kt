package com.el3asas.note.repositories.home

import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.data.source.local.daos.NoteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(private val noteDao: NoteDao) {
    fun getHomeNotes(): Flow<List<NoteEntity>?> = noteDao.getNotes()


    suspend fun getNoteById(noteId: Long): NoteEntity? = noteDao.getNoteById(noteId)

    suspend fun addNote(note: NoteEntity): NoteEntity {
        return note.copy(id = noteDao.insertNote(note))
    }

    suspend fun update(note: NoteEntity) {
        TODO("Not yet implemented")
    }

    suspend fun deleteNote(noteId: Long) {
        noteDao.deleteNote(noteId)
    }

}