package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.data.notes.persistance.NoteDBModel
import com.alorma.contactnotes.data.notes.persistance.NotesDAO
import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.Completable
import io.reactivex.Single

class RoomNotesDataSource(private val notesDAO: NotesDAO) : NotesDataSource {
    override fun getNotesByUser(userId: String): Single<List<Note>> {
        return notesDAO.getNotesByUser(userId).map {
            mapNotes(it)
        }
    }

    private fun mapNotes(models: List<NoteDBModel>) = models.map { mapNote(it) }

    private fun mapNote(noteDBModel: NoteDBModel) = Note(noteDBModel.content ?: "")

    override fun insertNote(userId: String, note: Note): Single<Note> {
        return Completable.fromAction {
            notesDAO.insertContact(mapToDBModel(userId, note))
        }.andThen(Single.just(note))
    }

    private fun mapToDBModel(userId: String, note: Note) = NoteDBModel(userId = userId, content = note.text)

}