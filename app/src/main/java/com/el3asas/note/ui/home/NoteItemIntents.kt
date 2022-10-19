package com.el3asas.note.ui.home

import android.view.View

sealed class NoteItemIntents {
    data class OpenNote(val v: View, val noteId: Long) : NoteItemIntents()
    data class DeleteNote(val noteId: Long) : NoteItemIntents()
}