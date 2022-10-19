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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    val lastEditDate = MutableStateFlow("")
    private val _mainStateView = MutableStateFlow<MainViewsState>(MainViewsState.Idle)
    val mainStateView: StateFlow<MainViewsState> get() = _mainStateView
    val isLoading = mutableStateOf(false)
    private val mainIntents = MutableSharedFlow<MainIntents>()

    var b = 1

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
                            viewModelScope.launch {
                                _mainStateView.emit(MainViewsState.NewNoteAdded(it))
                            }
                        }, onError = {
                            viewModelScope.launch {
                                _mainStateView.emit(MainViewsState.Error(it))
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