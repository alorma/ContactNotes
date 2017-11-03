package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.data.notes.store.NoteEntity
import com.alorma.contactnotes.data.notes.store.NotesDao
import com.alorma.contactnotes.domain.notes.Note
import java.util.*

class NotesProvider private constructor(private val db: NotesDao) {

    companion object {
        var INSTANCE: NotesProvider? = null

        fun init(db: NotesDao) {
            INSTANCE = NotesProvider(db)
        }
    }

    fun add(userId: String, note: Note): Either<Exception, Note> {
        return try {
            when {
                note.id == null -> Right(insert(note, userId))
                else -> Right(update(note, userId))
            }
        } catch (e: Exception) {
            Left(e)
        }
    }

    private fun insert(note: Note, userId: String): Note {
        val noteEntity = mapEntity(note, userId)
        db.insertAll(noteEntity)
        return mapNote(noteEntity)
    }

    private fun update(note: Note, userId: String): Note {
        val noteEntity = mapEntity(note, userId)
        db.update(noteEntity)
        return mapNote(noteEntity)
    }

    fun list(userId: String): List<Note> {
        return db.findByUserId(userId).map {
            mapNote(it)
        }
    }

    private fun mapNote(it: NoteEntity) = Note(it.id,
            it.content,
            Date(it.date))

    private fun mapEntity(it: Note, userId: String) = NoteEntity(it.id ?: UUID.randomUUID().toString(),
            userId,
            it.text,
            (it.date ?: Date()).time)
}