package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.data.contacts.ContactsDataSource
import io.reactivex.Flowable

class ContactRepository(private val system: ContactsDataSource) {
    fun getPhoto(lookup: String): Flowable<String> {
        return system.loadPhotoByLookup(lookup).toFlowable()
    }

}