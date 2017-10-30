package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.data.contacts.operations.ListContacts
import com.alorma.contactnotes.data.notes.ContactsNotesProvider
import com.alorma.contactnotes.data.notes.ListContactNotes
import javax.inject.Inject

class OverViewModelFactory @Inject constructor() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        return when (modelClass) {
            OverviewViewModel::class.java -> OverviewViewModel(provideListContacts(), provideListNotes()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideListContacts(): ListContacts {
        return ListContacts(ContactsListProvider.INSTANCE)
    }

    private fun provideListNotes(): ListContactNotes {
        return ListContactNotes(ContactsNotesProvider.INSTANCE)
    }
}