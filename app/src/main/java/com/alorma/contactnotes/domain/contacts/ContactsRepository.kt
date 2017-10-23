package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable
import io.reactivex.Single

class ContactsRepository(private val system: ContactsDataSource, private val appDs: ContactsDataSource) {
    fun getSavedContacts() {
        return appDs.getContacts()
    }

    fun insert(createUserForm: CreateUserForm) {
        appDs.insertContact(createUserForm)
    }

    fun loadContact(contactUri: String) {
        return system.loadContact(contactUri)
    }
}