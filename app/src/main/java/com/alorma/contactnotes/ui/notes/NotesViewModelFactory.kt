package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.notes.AddContactNote
import com.alorma.contactnotes.data.notes.NotesProvider
import com.alorma.contactnotes.data.notes.ListContactNotes

class NotesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ListNotesViewModel::class.java -> ListNotesViewModel(buildListContactNotes(), buildAddContactNotes()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildListContactNotes(): ListContactNotes {
        return ListContactNotes(NotesProvider.INSTANCE)
    }

    private fun buildAddContactNotes(): AddContactNote {
        return AddContactNote(NotesProvider.INSTANCE)
    }
}