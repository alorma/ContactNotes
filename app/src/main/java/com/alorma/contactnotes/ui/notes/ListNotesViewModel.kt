package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.notes.ErrorNotesLiveData
import com.alorma.contactnotes.data.notes.NotesListLiveData
import com.alorma.contactnotes.domain.GetNotesFromContactUseCase
import com.alorma.contactnotes.domain.notes.Note

class ListNotesViewModel(private val getNotesUseCase: GetNotesFromContactUseCase) : ViewModel() {

    fun getData(): LiveData<List<Note>> = NotesListLiveData.INSTANCE
    fun getError(): LiveData<Exception> = ErrorNotesLiveData.INSTANCE

    private lateinit var contactId: String

    fun load(contactId: String) {
        this.contactId = contactId

        getNotesUseCase.execute(contactId)

    }
}