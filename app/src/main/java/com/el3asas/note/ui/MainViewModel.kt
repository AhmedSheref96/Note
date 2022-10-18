package com.el3asas.note.ui

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.domain.home.HomeUseCases
import com.el3asas.note.repositories.home.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    val lastEditDate = MutableStateFlow("")

    fun addNote(v:View) {
        viewModelScope.launch {
            HomeUseCases.AddNote(
                Dispatchers.IO,
                repository,
                NoteEntity(
                    title = "title", images = null, date = "10/16/2022", description = "description"
                )
            )
        }
    }

}