package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact

class AppGetContact(private val contactsListProvider: ContactsListProvider) : GetContact {
    override fun get(userId: String): Contact? {
        return contactsListProvider.get(userId)
    }
}