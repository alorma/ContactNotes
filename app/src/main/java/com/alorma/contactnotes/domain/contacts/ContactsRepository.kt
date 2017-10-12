package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class ContactsRepository(private val system: ContactsDataSource, private val remote: ContactsDataSource) {
    fun getContacts(): Flowable<List<Contact>> {
        return system.getContacts()
    }

    fun getSavedContacts(): Flowable<List<Contact>> {
        return remote.getContacts()
    }

    fun insert(createUserForm: CreateUserForm): Completable {
        return remote.insertContact(createUserForm)
    }
}