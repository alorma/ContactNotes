package com.alorma.contactnotes.domain

import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Maybe

class GetNoteFromContactUseCase() {
    fun getNote(contactId: String): Maybe<Note> {
        return Maybe.empty()
    }
}