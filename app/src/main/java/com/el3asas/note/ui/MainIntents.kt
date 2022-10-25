package com.el3asas.note.ui

import android.view.View

sealed class MainIntents {
    data class AddNote(val V: View) : MainIntents()
}