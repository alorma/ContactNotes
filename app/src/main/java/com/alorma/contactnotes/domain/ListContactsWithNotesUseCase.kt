package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Flowable

class ListContactsWithNotesUseCase(private val contactsRepository: ContactsRepository,
                                   private val notesRepository: NotesRepository) {

    fun execute(): Flowable<List<Contact>> {
        return contactsRepository.getSavedContacts()
                .flatMap({ iterateContacts(it) })
    }

    private fun iterateContacts(contacts: List<Contact>): Flowable<MutableList<Contact>> {
        return Flowable.fromIterable(contacts)
                .flatMap(functionGetNotes(), functionCreateNewContactWithNotes())
                .toList()
                .toFlowable()
    }

    private fun functionGetNotes(): (Contact) -> Flowable<List<Note>> {
        return { contact -> getContactWithNotes(contact) }
    }

    private fun functionCreateNewContactWithNotes(): (Contact, List<Note>) -> Contact {
        return { contact, notes ->
            contact.copy(notes = notes)
        }
    }

    private fun getContactWithNotes(contact: Contact): Flowable<List<Note>> {
        return notesRepository.getNotesFromUser(contact.rawId)
                .toFlowable()
                .defaultIfEmpty(listOf())
    }
}