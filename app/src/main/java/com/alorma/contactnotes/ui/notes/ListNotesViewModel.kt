package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.arch.ListLiveData
import com.alorma.contactnotes.data.notes.AddContactNote
import com.alorma.contactnotes.data.notes.ListContactNotes
import com.alorma.contactnotes.data.notes.livedata.NotesLiveDataProvider
import com.alorma.contactnotes.domain.notes.Note

class ListNotesViewModel(private val listContactNotes: ListContactNotes,
                         private val addContactNote: AddContactNote,
                         private val notesLiveDataProvider: NotesLiveDataProvider) : ViewModel() {

    private val notesLiveData by lazy {
        notesLiveDataProvider.get(contactId)
    }

    private lateinit var contactId: String

    fun load(contactId: String): LiveData<List<Note>> {
        this.contactId = contactId
        val list = listContactNotes.list(contactId)

        notesLiveData?.clear()
        notesLiveData?.addAll(list)

        return Transformations.map(notesLiveData, { input -> input.toList() })
    }

    fun onNoteAdded() {
        val note = Note(text = "Fake text ${notesLiveData?.count()}")
        addContactNote.add(contactId, note)
        notesLiveData?.add(note)
    }
}