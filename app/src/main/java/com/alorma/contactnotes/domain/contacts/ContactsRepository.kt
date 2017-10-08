package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.data.contacts.ContactsDataSource
import io.reactivex.Flowable
import io.reactivex.Maybe

class ContactsRepository(private val system: ContactsDataSource, private val remote: ContactsDataSource) {
    fun getContacts(): Flowable<List<Contact>> {
        return system.getContacts()
    }

    fun getSavedContacts(): Flowable<List<Contact>> {
        return remote.getContacts()
    }

    fun insert(rawId: String): Maybe<Contact> {
        return system.getContactByRawId(rawId)
                .flatMap { remote.insertContact(it).andThen(Maybe.just(it)) }
    }
}