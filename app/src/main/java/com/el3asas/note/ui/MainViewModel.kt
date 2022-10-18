package com.el3asas.note.ui

import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.domain.home.HomeUseCases
import com.el3asas.note.repositories.home.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    val lastEditDate = MutableStateFlow("")
    private val _mainStateView = MutableStateFlow<MainStateViews>(MainStateViews.Idle)
    val mainStateView: StateFlow<MainStateViews> = _mainStateView
    val isLoading = mutableStateOf(false)
    private val mainIntents = MutableSharedFlow<MainIntents>()

    init {
        intentsProducer()
    }

    private fun intentsProducer() {
        viewModelScope.launch {
            mainIntents.asSharedFlow().collect { intent ->
                when (intent) {
                    is MainIntents.AddNote -> {
                        HomeUseCases.AddNote(Dispatchers.IO, repository, NoteEntity(
                            title = "title",
                            images = listOf("https://cnn-arabic-images.cnn.io/cloudinary/image/upload/w_1600,c_scale,q_auto/cnnarabic/2022/05/25/images/213287.webp"),
                            date = "10/16/2022",
                            description = "description"
                        ), onSuccess = {
                            Timber.d("------------------- new note added")
                            viewModelScope.launch {
                                _mainStateView.value = MainStateViews.NewNoteAdded(it)
                                Timber.d("------------------- new note added")
                            }
                        }, onError = {
                            viewModelScope.launch {
                                _mainStateView.value = MainStateViews.Error(it)
                            }
                        })
                    }
                }
            }
        }
    }

    fun addNote(v: View) {
        viewModelScope.launch {
            mainIntents.emit(MainIntents.AddNote)
        }
    }

}