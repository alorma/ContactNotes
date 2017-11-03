package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note

class AddContactNote(private val notesProvider: NotesProvider?) {
    fun add(userId: String, note: Note) {
        notesProvider?.add(userId, note)
    }
}