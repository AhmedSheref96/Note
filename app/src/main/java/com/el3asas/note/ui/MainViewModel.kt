package com.el3asas.note.ui

import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import com.el3asas.note.NavGraphDirections
import com.el3asas.note.R
import com.el3asas.note.repositories.home.NotesRepository
import com.el3asas.utils.utils.getActivity
import com.el3asas.utils.utils.navigate
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        intentsProducer()
    }

    private fun intentsProducer() {
        viewModelScope.launch {
            mainIntents.asSharedFlow().collect { intent ->
                when (intent) {
                    is MainIntents.AddNote -> {
                        navigate(
                            findNavController(
                                getActivity(intent.V.context)!!,
                                R.id.nav_host_fragment
                            ), NavGraphDirections.openAddDialog()
                        )
                    }
                }
            }
        }
    }

    fun addNote(v: View) {
        viewModelScope.launch {
            mainIntents.emit(MainIntents.AddNote(v))
        }
    }

}