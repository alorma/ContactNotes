package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.notes.Note
import java.util.*

class ListNotesViewModel : ViewModel() {

    private val items = mutableListOf<Note>()

    private val addNoteLiveData = MutableLiveData<Boolean>()
    private val notesLiveData = MutableLiveData<List<Note>>()

    fun getData(): LiveData<List<Note>> = notesLiveData

    fun load() {
        items.add(Note("AAAA"))
        items.add(Note("BBBB"))
        notesLiveData.postValue(items)
    }

    fun createNote() {
        items.add(Note(UUID.randomUUID().toString()))
        addNoteLiveData.postValue(true)
        notesLiveData.postValue(items)
    }

}