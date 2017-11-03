package com.alorma.contactnotes.data.notes.operations

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.data.notes.store.NotesProvider
import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Single

class ListContactNotes(private val notesProvider: NotesProvider?) {

    fun listSingle(userId: String): Single<Either<Throwable, List<Note>>> {
        return Single.fromCallable {
            when {
                notesProvider != null -> Right(list(userId))
                else -> Left(NullPointerException("NotesProvider is null"))
            }
        }
    }

    fun list(userId: String): List<Note> = notesProvider?.list(userId) ?: listOf()
}