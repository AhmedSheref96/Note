package com.el3asas.note.di

import android.content.Context
import androidx.room.Room
import com.el3asas.note.data.source.local.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocaleDiModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): RoomDataBase =
        Room.databaseBuilder(context, RoomDataBase::class.java,"MyNoteDB")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideNoteDao(roomDatabase: RoomDataBase) = roomDatabase.noteDao()

}