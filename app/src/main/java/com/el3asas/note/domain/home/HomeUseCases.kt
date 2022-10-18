package com.el3asas.note.domain.home

import androidx.lifecycle.LiveData
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.repositories.home.NotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber

sealed class HomeUseCases {
    object GetNotes : HomeUseCases() {
        operator fun invoke(
            repository: NotesRepository? = null
        ): LiveData<List<NoteEntity>?>? {
            return repository?.getHomeNotes()
        }
    }

    object AddNote : HomeUseCases() {
        suspend operator fun invoke(
            coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
            repository: NotesRepository? = null,
            note: NoteEntity,
            onError : (String)->Unit,
            onSuccess : (NoteEntity)->Unit
        ) {
            withContext(coroutineDispatcher) {
                try {
                    repository?.addNote(note)
                    onSuccess(note)
                }catch (e:Exception){
                    onError(e.message.toString())
                }
            }
        }
    }

    object UpdateNote : HomeUseCases() {
        suspend operator fun invoke(
            coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
            repository: NotesRepository? = null,
            note: NoteEntity
        ) {
            withContext(coroutineDispatcher) {
                repository?.update(note)
            }
        }
    }

}