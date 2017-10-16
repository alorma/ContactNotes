package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.contacts.ContactsRepository

class ListContactsWithNotesUseCase(private val contactsRepository: ContactsRepository) {

    fun execute() {
        return contactsRepository.getSavedContacts()
    }
}