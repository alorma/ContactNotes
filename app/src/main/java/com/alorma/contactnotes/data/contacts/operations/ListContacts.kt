package com.alorma.contactnotes.data.contacts.operations

import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Flowable

class ListContacts(private val contactsProvider: ContactsListProvider?) {

    fun list(): Flowable<List<Contact>> {
        return contactsProvider?.list() ?: Flowable.just(listOf())
    }

}