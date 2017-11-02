package com.alorma.contactnotes.ui.notes

import com.alorma.contactnotes.arch.BaseViewModel
import com.alorma.contactnotes.data.notes.AddContactNote
import com.alorma.contactnotes.data.notes.ListContactNotes

class ListNotesViewModel(private val listContactNotes: ListContactNotes,
                         private val addContactNote: AddContactNote) : BaseViewModel() {

    private lateinit var contactId: String

    fun load(contactId: String) {
        this.contactId = contactId
        listContactNotes.list(contactId)
    }

    fun onNoteAdded() {

    }
}