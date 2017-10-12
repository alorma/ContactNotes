package com.alorma.contactnotes.domain.notes;

import com.alorma.contactnotes.data.notes.FirebaseNotesDataSource
import io.reactivex.Flowable

class NotesRepository(private val remote: FirebaseNotesDataSource) {
    fun getNotesFromUser(userId: String): Flowable<List<Note>> {
        return remote.getNotesByUser(userId)
    }
}
