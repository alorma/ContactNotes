package com.alorma.contactnotes.data.notes.operations

import com.alorma.contactnotes.arch.*
import com.alorma.contactnotes.data.notes.store.NotesProvider
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.ui.note.NoteMetaData
import io.reactivex.Single

class GetContactNote(private val notesProvider: NotesProvider?) {

    fun getSingle(noteMetaData: NoteMetaData): Single<Either<Throwable, Note>> {
        return Single.fromCallable {
            when {
                notesProvider != null -> get(noteMetaData.noteId)
                else -> Left(NullPointerException("NotesProvider is null"))
            }
        }
    }

    private fun get(noteId: String?): Either<Throwable, Note>
            = noteId?.let { notesProvider?.getNoteById(noteId) } ?: Left(NoNoteException())
}