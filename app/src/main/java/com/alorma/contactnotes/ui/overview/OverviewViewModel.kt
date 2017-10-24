package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.contacts.ContactsLiveData
import com.alorma.contactnotes.data.contacts.ListContacts

class OverviewViewModel(private val listContacts: ListContacts) : ViewModel() {

    private val contactsLiveData = ContactsLiveData.INSTANCE

    fun loadContacts(): ContactsLiveData {
        contactsLiveData.value = listContacts.list()
        return contactsLiveData
    }
}