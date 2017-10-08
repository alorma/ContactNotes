package com.alorma.contactnotes.domain.notes;

import io.reactivex.Maybe

class NotesRepository {
    fun getNotesFromUser(userId: String): Maybe<List<Note>> {
        return Maybe.just(listOf(Note("$userId -  AAAAA")))
    }
}
