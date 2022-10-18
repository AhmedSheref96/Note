package com.el3asas.note.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.data.source.local.converters.NoteEntityConverter
import com.el3asas.note.data.source.local.daos.NoteDao

@Database(entities = [NoteEntity::class], exportSchema = true, version = 6)
@TypeConverters(NoteEntityConverter::class)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}