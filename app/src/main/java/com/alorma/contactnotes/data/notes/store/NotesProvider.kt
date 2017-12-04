package com.alorma.contactnotes.data.notes.store

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.data.notes.operations.NoNoteException
import com.alorma.contactnotes.domain.notes.Note
import java.util.*

class NotesProvider private constructor(private val db: NotesDao) {

    companion object {
        var INSTANCE: NotesProvider? = null

        fun init(db: NotesDao) {
            INSTANCE = NotesProvider(db)
        }
    }

    fun list(userId: String): List<Note> {
        return db.findByUserId(userId).map {
            mapNote(it)
        }.sortedByDescending { it.date }
    }

    fun count(userId: String): Int {
        return list(userId).size
    }

    fun getNoteById(noteId: String): Either<Throwable, Note> {
        val noteEntity = db.findById(noteId)
        return if (noteEntity == null) {
            Left(NoNoteException())
        } else {
            Right(mapNote(noteEntity))
        }
    }

    fun insertNote(note: String, contactId: String): Either<Exception, Note> {
        return try {
            Right(insert(note, contactId))
        } catch (e: Exception) {
            Left(e)
        }
    }

    private fun insert(text: String, contactId: String): Note {
        val noteEntity = NoteEntity(contactId = contactId, content = text)
        db.insertAll(noteEntity)
        return mapNote(noteEntity)
    }

    fun updateNote(text: String, noteId: String, contactId: String): Either<Exception, Note> {
        return try {
            Right(update(text, noteId, contactId))
        } catch (e: Exception) {
            Left(e)
        }
    }

    private fun update(text: String, noteId: String, contactId: String): Note {
        val noteEntity = NoteEntity(noteId.toLong(), contactId, text, System.currentTimeMillis())
        db.update(noteEntity)
        return mapNote(noteEntity)
    }

    private fun mapNote(it: NoteEntity) = Note(it.id.toString(),
            it.content,
            Date(it.date))
}