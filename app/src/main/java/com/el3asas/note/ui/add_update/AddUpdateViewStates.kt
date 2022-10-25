package com.el3asas.note.ui.add_update

import com.el3asas.note.data.models.NoteEntity

sealed class AddUpdateViewStates {
    object Idle : AddUpdateViewStates()
    object Loading : AddUpdateViewStates()
    object Error : AddUpdateViewStates()
    data class CompleteAddNote(val noteId:Long) : AddUpdateViewStates()
    data class GetNoteData(val noteEntity: NoteEntity) : AddUpdateViewStates()
}