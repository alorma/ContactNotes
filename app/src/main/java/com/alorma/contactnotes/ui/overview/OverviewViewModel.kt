package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.contacts.ContactsLiveData
import com.alorma.contactnotes.data.contacts.ListContacts
import com.alorma.contactnotes.data.notes.ListContactNotes

class OverviewViewModel(private val listContacts: ListContacts,
                        private val listContactNotes: ListContactNotes) : ViewModel() {

    private val contactsLiveData = ContactsLiveData.INSTANCE

    fun loadContacts(): ContactsLiveData {
        contactsLiveData.value = listContacts.list().map {
            val notes = listContactNotes.list(it.id)
            it.copy(notes = notes)
        }
        return contactsLiveData
    }
}