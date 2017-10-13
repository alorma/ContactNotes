package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface NotesDataSource {
    fun insertNote(userId: String, note: Note): Completable
    fun getNotesByUser(userId: String): Flowable<List<Note>>
    fun getNote(noteId: String): Single<Note>
    fun createNote(contactId: String): Single<String>
    fun createNote(contactId: String, text: String): Single<String>
    fun updateNote(contactId: String, noteId: String, text: String): Single<String>
}