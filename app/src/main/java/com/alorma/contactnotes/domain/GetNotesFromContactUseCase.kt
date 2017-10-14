package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.NotesRepository

class GetNotesFromContactUseCase(private val notesRepository: NotesRepository) {
    fun execute(contactId: String) {
        notesRepository.getNotesByUser(contactId)
    }
}
