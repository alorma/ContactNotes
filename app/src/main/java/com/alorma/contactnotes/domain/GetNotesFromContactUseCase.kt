package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Flowable

class GetNotesFromContactUseCase(private val notesRepository: NotesRepository) {
    fun execute(contactId: String): Flowable<List<Note>> {
        return notesRepository.getNotesFromUser(contactId)
                .defaultIfEmpty(listOf())
    }
}
