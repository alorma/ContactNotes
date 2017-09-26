package com.alorma.contactnotes.domain;

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Maybe

class InsertContactUseCase(private val contactsRepository: ContactsRepository, private val notesRepository: NotesRepository) {

    fun execute(rawId: String): Maybe<Contact> {
        return contactsRepository.insert(rawId)
                .flatMap { contact ->
                    notesRepository.addDefaultNote(contact.rawId)
                            .flatMapMaybe {
                                val newContact = contact.copy(notes = listOf(it))
                                Maybe.just(newContact)
                            }
                }
    }

}
