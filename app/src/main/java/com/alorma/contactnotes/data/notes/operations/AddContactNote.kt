package com.alorma.contactnotes.data.notes.operations

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.data.notes.store.NotesProvider
import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Single

class AddContactNote(private val notesProvider: NotesProvider?) {
    fun add(text: String,contactId: String): Single<Either<Throwable, Note>> {
        return Single.fromCallable {
            when {
                notesProvider != null -> notesProvider.insertNote(text, contactId)
                else -> Left(NullPointerException("NotesProvider is null"))
            }
        }
    }
}