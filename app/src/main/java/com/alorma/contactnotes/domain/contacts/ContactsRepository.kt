package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ContactsRepository(private val system: ContactsDataSource, private val remote: ContactsDataSource) {
    fun getContacts(): Flowable<List<Contact>> {
        return system.getContacts()
    }

    fun getSavedContacts(): Flowable<List<Contact>> {
        return remote.getContacts()
    }

    fun insert(createUserForm: CreateUserForm): Completable {
        return if (createUserForm.lookup != null) {
            remote.loadContactByLookup(createUserForm.lookup)
                    .flatMapCompletable { remote.update(it.id, createUserForm) }
                    .onErrorResumeNext {
                        when (it) {
                            is NoSuchElementException -> remote.insertContact(createUserForm)
                            else -> Completable.error(it)
                        }
                    }

        } else {
            remote.insertContact(createUserForm)
        }
    }

    fun loadContact(contactUri: String): Single<Contact> {
        return system.getLookupKey(contactUri).flatMap { system.loadContactByLookup(it) }
    }
}