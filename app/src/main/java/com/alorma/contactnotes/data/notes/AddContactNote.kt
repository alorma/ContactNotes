package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note

class AddContactNote(private val contactsNotesProvider: ContactsNotesProvider?) {
    fun add(userId: String, note: Note) {
        contactsNotesProvider?.add(userId, note)
    }
}