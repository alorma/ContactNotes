package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note

class ListContactNotes(private val contactsNotesProvider: ContactsNotesProvider) {
    fun list(userId: String): List<Note> {
        return contactsNotesProvider.list(userId)
    }
}