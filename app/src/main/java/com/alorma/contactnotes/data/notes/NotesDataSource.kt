package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Single

interface NotesDataSource {
    fun insertNote(userId: String, note: Note): Single<Note>
    fun getNotesByUser(userId: String): Single<List<Note>>
}