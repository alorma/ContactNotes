package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Completable
import io.reactivex.Flowable

interface NotesDataSource {
    fun insertNote(userId: String, note: Note): Completable
    fun getNotesByUser(userId: String): Flowable<List<Note>>
}