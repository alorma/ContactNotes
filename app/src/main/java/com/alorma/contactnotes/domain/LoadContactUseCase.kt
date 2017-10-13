package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import io.reactivex.Single

class LoadContactUseCase(private val contactsRepository: ContactsRepository) {

    fun execute(contactUri: String): Single<Contact> {
        return contactsRepository.loadContact(contactUri)
    }

}