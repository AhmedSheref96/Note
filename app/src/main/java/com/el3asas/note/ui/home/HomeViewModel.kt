package com.el3asas.note.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el3asas.note.domain.home.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val homeAdapter = HomeAdapter()

    init {
        viewModelScope.launch {
            HomeUseCases.GetNotes.invoke()?.map {
                NoteItemStateUi(it.title, it.description, it.images?.get(0), date = it.date)
            }?.let {
                homeAdapter.setData(it)
            }
        }
    }

}