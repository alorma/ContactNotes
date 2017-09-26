package com.alorma.contactnotes.domain.notes

import com.alorma.contactnotes.data.notes.NotesDataSource
import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

class NotesRepository(private val dataSource: NotesDataSource) {
    fun addDefaultNote(userId: String): Single<Note> {
        return dataSource.insertNote(userId, Note("Example first note for user $userId"))
    }

    fun getNotesFromUser(userId: String): Single<List<Note>> {
        return dataSource.getNotesByUser(userId)
    }
}