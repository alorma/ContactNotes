package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.notes.NotesRepository
import io.reactivex.Single

class LoadNoteUseCase(private val notesRepository: NotesRepository) {
    fun execute(noteId: String): Single<Note> = notesRepository.getNote(noteId)
}