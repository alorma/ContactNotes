package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.data.notes.store.NotesDao
import com.alorma.contactnotes.domain.notes.Note

class ContactsNotesProvider private constructor(private val notesDao: NotesDao) {

    companion object {
        var INSTANCE: ContactsNotesProvider? = null

        fun init(db: NotesDao) {
            INSTANCE = ContactsNotesProvider(db)
        }
    }

    fun add(userId: String, note: Note) {

    }

    fun list(userId: String): List<Note> {
        return listOf()
    }
}