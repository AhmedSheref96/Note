package com.el3asas.note.domain.home

import androidx.lifecycle.LiveData
import com.el3asas.note.data.models.NoteEntity
import com.el3asas.note.repositories.home.NotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class HomeUseCases {
    object GetNotes : HomeUseCases() {
        operator fun invoke(
            repository: NotesRepository? = null
        ): LiveData<List<NoteEntity>?>? {
            return repository?.getHomeNotes()
        }
    }

    object AddNote : HomeUseCases() {
        private var counter = 0
        suspend operator fun invoke(
            coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
            repository: NotesRepository? = null,
            note: NoteEntity,
            onError: (String) -> Unit,
            onSuccess: (NoteEntity) -> Unit
        ) {
            withContext(coroutineDispatcher) {
                try {
                    onSuccess(repository?.addNote(note.copy(title = note.title + counter++))!!)
                } catch (e: Exception) {
                    onError(e.message.toString())
                }
            }
        }
    }

    object DeleteNote : HomeUseCases() {
        suspend operator fun invoke(
            coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
            repository: NotesRepository? = null,
            noteId: Long,
            onError: (String) -> Unit,
            onSuccess: () -> Unit
        ) {
            withContext(coroutineDispatcher) {
                try {
                    repository?.deleteNote(noteId)!!
                    onSuccess()
                } catch (e: Exception) {
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