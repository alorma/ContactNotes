package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Single

class UpdateNoteUseCase(private val notesRepository: NotesRepository) {
    fun execute(contactId: String, noteId: String, text: String) {
        notesRepository.updateNote(contactId, noteId, text)
    }

}