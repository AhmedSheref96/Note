package com.el3asas.note.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.el3asas.note.domain.home.HomeUseCases
import com.el3asas.note.repositories.home.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {
    val homeIntents = Channel<HomeIntents>(Channel.UNLIMITED)
    private val noteItemIntents = Channel<NoteItemIntents>(Channel.UNLIMITED)
    val homeAdapter = HomeAdapter(viewModelScope, noteItemIntents)

    init {
        getData()
        handleNoteState()
        homeIntentState()
    }

    private fun getData() {
        viewModelScope.launch {
            HomeUseCases.GetNotes.invoke(repository)?.collect { noteEntities ->
                noteEntities?.map {
                    NoteItemStateUi(
                        it.id ?: 0,
                        it.title,
                        it.description,
                        it.images?.get(0),
                        colorHex = it.colorHex,
                        date = it.date
                    )
                }?.let {
                    homeAdapter.setData(it)
                }
            }
        }
    }

    private fun handleNoteState() {
        viewModelScope.launch {
            noteItemIntents.consumeAsFlow().collect {
                when (it) {
                    is NoteItemIntents.DeleteNote -> {
                        HomeUseCases.DeleteNote(
                            Dispatchers.IO,
                            repository,
                            it.noteId,
                            onSuccess = {},
                            onError = {})
                    }

                    is NoteItemIntents.OpenNote -> {
                        it.v.findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToAddUpdateFragment(noteId = it.noteId)
                        )
                    }
                }
            }
        }
    }

    private fun homeIntentState() {
        viewModelScope.launch {
            homeIntents.consumeAsFlow().collect {
                when (it) {
                    is HomeIntents.Refresh -> {
                        getData()
                        it.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

}