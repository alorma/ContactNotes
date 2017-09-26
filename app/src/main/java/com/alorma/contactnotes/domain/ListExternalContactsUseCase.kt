package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Flowable

class ListExternalContactsUseCase(private val contactsRepository: ContactsRepository) {

    fun execute(): Flowable<List<Contact>> {
        return contactsRepository.getContacts()
    }

}