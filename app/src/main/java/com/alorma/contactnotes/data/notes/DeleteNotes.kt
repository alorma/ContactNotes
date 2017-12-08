package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.data.notes.store.NotesProvider
import io.reactivex.Single

class DeleteNotes(private val notesProvider: NotesProvider?) {
    fun delete(userId: String, notesSet: Set<String>): Single<Either<Exception, Boolean>> {
        return Single.fromCallable {
            when {
                notesProvider != null -> deleteNotes(userId, notesProvider, notesSet)
                else -> Left(NullPointerException("NotesProvider is null"))
            }
        }
    }

    private fun deleteNotes(userId: String,
                            notesProvider: NotesProvider,
                            notesSet: Set<String>): Either<Exception, Boolean> {
        return notesProvider.delete(userId, notesSet.mapNotNull { notesProvider.getNote(it) })
    }
}