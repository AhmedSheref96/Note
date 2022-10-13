package com.el3asas.note.domain.home

import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.repositories.home.NotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class HomeUseCases @Inject constructor(
    val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    val repository: NotesRepository? = null
) {

    object GetNotes : HomeUseCases() {
        suspend operator fun invoke() : List<NoteEntity>? {
            return withContext(coroutineDispatcher) {
                repository?.getHomeNotes()
            }
        }
    }

    object AddNote : HomeUseCases() {
        suspend operator fun invoke(note: NoteEntity) {
            withContext(coroutineDispatcher) {
                repository?.addNote(note)
            }
        }
    }

    object UpdateNote : HomeUseCases() {
        suspend operator fun invoke(note: NoteEntity) {
            withContext(coroutineDispatcher) {
                repository?.update(note)
            }
        }
    }

}