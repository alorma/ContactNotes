package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.NotesRepository

class InsertNoteUseCase(private val notesRepository: NotesRepository) {
    fun execute(contactId: String) {
        notesRepository.createNote(contactId)
    }

    fun execute(contactId: String, text: String) {
        notesRepository.createNote(contactId, text)
    }

}