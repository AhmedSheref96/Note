package com.el3asas.note.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.data.source.local.daos.NoteDao

@Database(entities = [NoteEntity::class], exportSchema = false, version = 1)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}