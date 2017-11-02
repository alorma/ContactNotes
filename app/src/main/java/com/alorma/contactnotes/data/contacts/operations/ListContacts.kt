package com.alorma.contactnotes.data.contacts.operations

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Observable

class ListContacts(private val contactsProvider: ContactsListProvider?) {

    fun list(): Observable<Either<Throwable, List<Contact>>> {
        return Observable.fromCallable {
            contactsProvider?.list() ?: Left(NullPointerException("ContactsListProvider is null"))
        }
    }

}