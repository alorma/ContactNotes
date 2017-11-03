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

    fun add(note: Note): Either<Exception, Note> {
        return try {
            when {
                note.id == null -> Right(insert(note))
                else -> Right(update(note))
            }
        } catch (e: Exception) {
            Left(e)
        }
    }

    private fun insert(note: Note): Note {
        val noteEntity = mapEntity(note)
        db.insertAll(noteEntity)
        return mapNote(noteEntity)
    }

    private fun update(note: Note): Note {
        val noteEntity = mapEntity(note)
        db.update(noteEntity)
        return mapNote(noteEntity)
    }

    fun list(userId: String): List<Note> {
        return db.findByUserId(userId).map {
            mapNote(it)
        }
    }

    fun getNoteById(noteId: String): Either<Throwable, Note> {
        val noteEntity = db.findById(noteId)
        return if (noteEntity == null) {
            Left(NoNoteException())
        } else {
            Right(mapNote(noteEntity))
        }
    }

    private fun mapNote(it: NoteEntity) = Note(it.id,
            it.content,
            it.contactId,
            Date(it.date))

    private fun mapEntity(it: Note) = NoteEntity(it.id ?: UUID.randomUUID().toString(),
            it.contactId,
            it.text,
            (it.date ?: Date()).time)
}