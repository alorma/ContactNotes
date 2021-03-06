package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.notes.DeleteNotes
import com.alorma.contactnotes.data.notes.operations.AddContactNote
import com.alorma.contactnotes.data.notes.store.NotesProvider
import com.alorma.contactnotes.data.notes.operations.ListContactNotes

class NotesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ListNotesViewModel::class.java -> ListNotesViewModel(buildListContactNotes(), buildDeleteContactNotes()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildListContactNotes(): ListContactNotes {
        return ListContactNotes(NotesProvider.INSTANCE)
    }

    private fun buildDeleteContactNotes(): DeleteNotes {
        return DeleteNotes(NotesProvider.INSTANCE)
    }
}