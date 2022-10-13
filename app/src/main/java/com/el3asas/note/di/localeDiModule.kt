package com.el3asas.note.di

import android.content.Context
import androidx.room.Room
import com.el3asas.note.data.source.local.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@Singleton
object LocaleDiModule {

    @Provides
    fun provideRoomDataBase(@ApplicationContext context: Context): RoomDataBase =
        Room.databaseBuilder(context, name = "", klass = RoomDataBase::class.java)
            .build()

    @Provides
    fun provideNoteDao(roomDatabase: RoomDataBase) = roomDatabase.noteDao()

}