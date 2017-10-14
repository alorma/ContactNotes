package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface NotesDataSource {
    fun getNotesFromUser(userId: String): Flowable<List<Note>>
    fun getNotesByUser(userId: String)
    fun getNote(noteId: String)
    fun createNote(contactId: String)
    fun createNote(contactId: String, text: String)
    fun updateNote(contactId: String, noteId: String, text: String)
}
