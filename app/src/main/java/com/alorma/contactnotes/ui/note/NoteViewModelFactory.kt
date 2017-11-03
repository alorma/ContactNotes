package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.notes.operations.AddContactNote
import com.alorma.contactnotes.data.notes.operations.GetContactNote
import com.alorma.contactnotes.data.notes.store.NotesProvider

class NoteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NoteViewModel::class.java -> NoteViewModel(buildGetNote(), buildAddNote()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildGetNote(): GetContactNote = GetContactNote(NotesProvider.INSTANCE)

    private fun buildAddNote(): AddContactNote = AddContactNote(NotesProvider.INSTANCE)


}