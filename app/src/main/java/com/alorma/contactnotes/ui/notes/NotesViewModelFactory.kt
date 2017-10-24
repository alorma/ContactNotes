package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.notes.AddContactNote
import com.alorma.contactnotes.data.notes.ContactsNotesProvider
import com.alorma.contactnotes.data.notes.ListContactNotes
import com.alorma.contactnotes.data.notes.livedata.NotesLiveDataProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class NotesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ListNotesViewModel::class.java -> ListNotesViewModel(buildListContactNotes(), buildAddContactNotes(), buildNOtesLiveDataProvider()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildListContactNotes(): ListContactNotes {
        return ListContactNotes(ContactsNotesProvider.INSTANCE)
    }

    private fun buildAddContactNotes(): AddContactNote {
        return AddContactNote(ContactsNotesProvider.INSTANCE)
    }

    private fun buildNOtesLiveDataProvider(): NotesLiveDataProvider {
        return NotesLiveDataProvider.INSTANCE
    }
}