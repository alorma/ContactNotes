package com.alorma.contactnotes.domain;

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import io.reactivex.Maybe

class InsertContactUseCase(private val contactsRepository: ContactsRepository) {

    fun execute(rawId: String): Maybe<Contact> {
        return contactsRepository.insert(rawId)
    }

}
