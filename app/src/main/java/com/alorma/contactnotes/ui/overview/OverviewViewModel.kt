package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.contacts.livedata.ContactsLiveData
import com.alorma.contactnotes.data.contacts.operations.ListContacts
import com.alorma.contactnotes.data.notes.ListContactNotes

class OverviewViewModel(private val listContacts: ListContacts,
                        private val listContactNotes: ListContactNotes) : ViewModel() {

    val contactsLiveData = ContactsLiveData.INSTANCE

    fun loadContacts() {
        contactsLiveData.value = listContacts.list().map {
            val notes = listContactNotes.list(it.id)
            it.copy(notes = notes)
        }
    }
}