package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.notes.AddContactNote
import com.alorma.contactnotes.data.notes.ListContactNotes
import com.alorma.contactnotes.data.notes.livedata.NotesLiveDataProvider
import com.alorma.contactnotes.domain.notes.Note

class ListNotesViewModel(private val listContactNotes: ListContactNotes,
                         private val addContactNote: AddContactNote,
                         private val notesLiveDataProvider: NotesLiveDataProvider) : ViewModel() {

    val notesLiveData by lazy {
        notesLiveDataProvider.get(contactId)
    }

    private lateinit var contactId: String

    fun load(contactId: String) {
        this.contactId = contactId
        listContactNotes.list(contactId)
    }

    fun onNoteAdded() {
        val note = Note(text = "Fake text ${notesLiveData?.count()}")
        addContactNote.add(contactId, note)
    }
}