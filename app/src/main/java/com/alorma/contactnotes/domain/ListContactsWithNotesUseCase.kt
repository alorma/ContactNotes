package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Flowable

class ListContactsWithNotesUseCase(private val contactsRepository: ContactsRepository,
                                   private val notesRepository: NotesRepository) {

    fun execute(): Flowable<List<Contact>> {
        return contactsRepository.getSavedContacts()
                .flatMap { contacts ->
                    Flowable.fromIterable(contacts)
                            .flatMap({ contact -> getContactWithNotes(contact) })
                            .toList()
                            .toFlowable()
                }
    }

    private fun getContactWithNotes(contact: Contact): Flowable<Contact> {
        return notesRepository.getNotesFromUser(contact.rawId)
                .map { notes -> contact.copy(notes = notes) }
                .toFlowable()
    }
}