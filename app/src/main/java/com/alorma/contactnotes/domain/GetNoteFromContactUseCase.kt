package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Maybe

class GetNoteFromContactUseCase(private val notesRepository: NotesRepository) {
    fun getNote(contactId: String): Maybe<Note> {
        return notesRepository.getNotesFromUser(contactId)
                .flatMapMaybe { if (it.isNotEmpty()) Maybe.just(it[0]) else Maybe.empty() }
    }
}