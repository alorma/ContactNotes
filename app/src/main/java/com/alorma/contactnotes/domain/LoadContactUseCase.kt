package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.contacts.ContactsRepository

class LoadContactUseCase(private val contactsRepository: ContactsRepository) {

    fun execute(contactUri: String) {
        contactsRepository.loadContact(contactUri)
    }

}