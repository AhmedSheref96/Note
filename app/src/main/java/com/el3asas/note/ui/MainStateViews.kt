package com.el3asas.note.ui

import com.el3asas.note.data.models.NoteEntity

sealed class MainStateViews {
    object Idle : MainStateViews()
    object Loading : MainStateViews()
    data class Error(val message: String) : MainStateViews()
    data class NewNoteAdded(val noteEntity: NoteEntity) : MainStateViews()
}
