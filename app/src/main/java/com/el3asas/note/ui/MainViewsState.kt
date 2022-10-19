package com.el3asas.note.ui

import com.el3asas.note.data.models.NoteEntity

sealed class MainViewsState {
    object Idle : MainViewsState()
    object Loading : MainViewsState()
    data class Error(val message: String) : MainViewsState()
    data class NewNoteAdded(val noteEntity: NoteEntity) : MainViewsState()
}
