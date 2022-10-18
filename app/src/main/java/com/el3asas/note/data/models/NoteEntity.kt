package com.el3asas.note.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val title:String,
    val description:String,
    val images :List<String>?,
    val date:String)
