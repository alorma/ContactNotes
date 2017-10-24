package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.arch.ListLiveData
import com.alorma.contactnotes.data.notes.ListContactNotes
import com.alorma.contactnotes.domain.notes.Note

class ListNotesViewModel(private val listContactNotes: ListContactNotes) : ViewModel() {

    private val notesLiveData = ListLiveData<Note>()

    fun load(contactId: String): LiveData<List<Note>> {
        val list = listContactNotes.list(contactId)

        notesLiveData.clear()
        notesLiveData.addAll(list)

        return Transformations.map(notesLiveData, { input -> input.toList() })
    }
}