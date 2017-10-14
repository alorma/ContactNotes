package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.NotesRepository

class LoadNoteUseCase(private val notesRepository: NotesRepository) {
    fun execute(noteId: String) {
        notesRepository.getNote(noteId)
    }
}