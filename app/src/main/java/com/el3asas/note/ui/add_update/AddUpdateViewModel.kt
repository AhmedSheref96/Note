package com.el3asas.note.ui.add_update

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.domain.home.HomeUseCases
import com.el3asas.note.repositories.home.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateViewModel @Inject constructor(private val repository: NotesRepository) :
    ViewModel() {
    @SuppressLint("MutableCollectionMutableState")
    val images = mutableStateOf<List<Uri>>(ArrayList())
    val addUpdateIntents = Channel<AddUpdateIntents>(Channel.UNLIMITED)
    private val _addUpdateViewStates =
        mutableStateOf<AddUpdateViewStates>(AddUpdateViewStates.Idle)
    val addUpdateViewStates get() = _addUpdateViewStates

    init {
        processIntents()
    }

    private fun processIntents() {
        viewModelScope.launch {
            addUpdateIntents.consumeAsFlow().collect {
                when (it) {
                    is AddUpdateIntents.AddOrUpdateNote -> {
                        _addUpdateViewStates.value = AddUpdateViewStates.Loading
                        addOrUpdateNote(it.note)
                    }
                    is AddUpdateIntents.OpenNote -> {}
                    is AddUpdateIntents.ClearData -> {}
                    is AddUpdateIntents.GetNoteData -> {
                        HomeUseCases.GetNote(it.noteId)
                            .invoke(coroutineDispatcher = Dispatchers.IO, repository = repository)
                            ?.let { it1 -> AddUpdateViewStates.GetNoteData(it1) }?.let { note ->
                                _addUpdateViewStates.value = note
                            }
                    }
                    is AddUpdateIntents.IdleView -> {
                        _addUpdateViewStates.value = AddUpdateViewStates.Idle
                    }
                }
            }
        }
    }

    private fun addOrUpdateNote(noteEntity: NoteEntity) {
        viewModelScope.launch {
            addUpdateViewStates.value = AddUpdateViewStates.Loading
            HomeUseCases.AddNote(
                coroutineDispatcher = Dispatchers.IO,
                repository = repository,
                note = noteEntity,
                onError = {
                    viewModelScope.launch {
                        addUpdateViewStates.value = AddUpdateViewStates.Error
                    }
                },
                onSuccess = {
                    viewModelScope.launch {
                        addUpdateViewStates.value = AddUpdateViewStates.CompleteAddNote(it.id!!)
                    }
                }
            )
        }

    }
}