package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.notes.ErrorNotesLiveData
import com.alorma.contactnotes.data.notes.livedata.InsertNoteLiveData
import com.alorma.contactnotes.data.notes.SingleNoteLiveData
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.validator.Validator

class NoteViewModel(private val noteValidator: Validator<String, String>) : ViewModel() {

    fun getData(): LiveData<Note> = SingleNoteLiveData.INSTANCE
    fun getError(): LiveData<Exception> = ErrorNotesLiveData.INSTANCE

    private var newNote = false

    fun newNote() {
        newNote = true
    }

    private lateinit var noteId: String

    fun loadNote(noteId: String) {

    }

    fun saveNote(contactId: String, text: String): LiveData<String> {
        return InsertNoteLiveData.INSTANCE
    }
}
