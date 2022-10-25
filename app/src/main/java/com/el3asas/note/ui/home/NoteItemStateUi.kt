package com.el3asas.note.ui.home

data class NoteItemStateUi(
    val id: Long,
    val title: String,
    val description: String,
    val imageString: String?,
    val colorHex: Long,
    val date: String
)