package com.el3asas.note.ui.home

sealed class NoteItemIntents {
    object OpenNote : NoteItemIntents()
    data class DeleteNote(val noteId: Int) : NoteItemIntents()
}