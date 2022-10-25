package com.el3asas.note.ui.add_update

import com.el3asas.note.data.models.NoteEntity

sealed class AddUpdateIntents {
    data class AddOrUpdateNote(val note: NoteEntity) : AddUpdateIntents()
    data class GetNoteData(val noteId:Long) : AddUpdateIntents()
    object ClearData : AddUpdateIntents()
    object IdleView : AddUpdateIntents()
    object OpenNote : AddUpdateIntents()
}