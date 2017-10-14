package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.notes.ErrorNotesLiveData
import com.alorma.contactnotes.data.notes.InsertNoteLiveData
import com.alorma.contactnotes.data.notes.SingleNoteLiveData
import com.alorma.contactnotes.domain.InsertNoteUseCase
import com.alorma.contactnotes.domain.LoadNoteUseCase
import com.alorma.contactnotes.domain.UpdateNoteUseCase
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.validator.Validator

class NoteViewModel(private val loadNoteUseCase: LoadNoteUseCase,
                    private val insertNoteUseCase: InsertNoteUseCase,
                    private val updateNoteUseCase: UpdateNoteUseCase,
                    private val noteValidator: Validator<String, String>) : ViewModel() {

    fun getData(): LiveData<Note> = SingleNoteLiveData.INSTANCE
    fun getError(): LiveData<Exception> = ErrorNotesLiveData.INSTANCE

    private var newNote = false

    fun newNote() {
        newNote = true
    }

    private lateinit var noteId: String

    fun loadNote(noteId: String) {
        this.noteId = noteId
        newNote = false

        loadNoteUseCase.execute(noteId)
    }

    fun saveNote(contactId: String, text: String): LiveData<String> {
        if (noteValidator.validate(text)) {
            if (newNote) {
                insertNoteUseCase.execute(contactId, text)
            } else {
                updateNoteUseCase.execute(contactId, noteId, text)
            }
        }
        return InsertNoteLiveData.INSTANCE
    }
}
