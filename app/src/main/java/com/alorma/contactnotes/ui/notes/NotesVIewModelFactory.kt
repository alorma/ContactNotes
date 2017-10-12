package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.domain.GetNotesFromContactUseCase
import com.alorma.contactnotes.domain.notes.NotesRepository

class NotesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ListNotesViewModel::class.java -> ListNotesViewModel(buildGetNotesUseCase()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildGetNotesUseCase(): GetNotesFromContactUseCase {
        val notesRepository = NotesRepository()
        return GetNotesFromContactUseCase(notesRepository)
    }
}