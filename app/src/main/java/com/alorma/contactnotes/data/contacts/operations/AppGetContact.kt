package com.alorma.contactnotes.data.contacts.operations

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.domain.contacts.Contact

class AppGetContact(private val contactsListProvider: ContactsListProvider) {
    fun get(userId: String): Either<Contact, Exception> {
        return contactsListProvider.get(userId)
    }
}