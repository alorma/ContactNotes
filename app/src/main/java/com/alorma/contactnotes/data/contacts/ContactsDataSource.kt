package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface ContactsDataSource {
    fun getContacts(): Flowable<List<Contact>>

    fun getContactByRawId(rawId: String): Maybe<Contact>

    fun insertContact(it: Contact): Completable
}