package com.alorma.contactnotes.data.contacts.operations

import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.domain.contacts.Contact

class ListContacts(private val contactsProvider: ContactsListProvider?) {

    fun list(): List<Contact> {
        return contactsProvider?.list() ?: listOf()
    }

}