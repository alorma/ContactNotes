package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.*

interface ContactsDataSource {
    fun getContacts(): Flowable<List<Contact>>

    fun getContactByRawId(rawId: String): Maybe<Contact>

    fun insertContact(createUserForm: CreateUserForm): Completable

    fun update(id: String, createUserForm: CreateUserForm): Completable

    fun loadContactByLookup(lookup: String): Single<Contact>

    fun getLookupKey(contactUri: String): Single<String>
}