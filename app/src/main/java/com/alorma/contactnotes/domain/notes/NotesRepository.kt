package com.alorma.contactnotes.domain.notes;

import com.alorma.contactnotes.data.notes.NotesDataSource
import io.reactivex.Flowable
import io.reactivex.Single

class NotesRepository(private val remote: NotesDataSource) {
    fun getNotesFromUser(userId: String): Flowable<List<Note>> {
        return remote.getNotesByUser(userId)
    }

    fun getNote(noteId: String) = remote.getNote(noteId)

    fun createNote(contactId: String): Single<String> {
        return remote.createNote(contactId)
    }

    fun createNote(contactId: String, text: String): Single<String> {
        return remote.createNote(contactId, text)
    }

    fun updateNote(contactId: String, noteId: String, text: String): Single<String> {
        return remote.updateNote(contactId, noteId, text)
    }
}
