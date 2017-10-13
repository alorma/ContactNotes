package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Single

class InsertNoteUseCase(private val notesRepository: NotesRepository) {
    fun execute(contactId: String): Single<String> {
        return notesRepository.createNote(contactId)
    }

    fun execute(contactId: String, text: String): Single<String> {
        return notesRepository.createNote(contactId, text)
    }

}