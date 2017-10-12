package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.GetNotesFromContactUseCase
import com.alorma.contactnotes.domain.notes.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ListNotesViewModel(private val getNotesUseCase: GetNotesFromContactUseCase) : ViewModel() {

    private val items = mutableListOf<Note>()

    private val addNoteLiveData = MutableLiveData<Boolean>()
    private val notesLiveData = MutableLiveData<List<Note>>()

    fun getData(): LiveData<List<Note>> = notesLiveData

    fun load(contactId: String) {
        getNotesUseCase.execute(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    items.clear()
                    items.addAll(it)
                    notesLiveData.postValue(it)
                }, {})
    }

    fun createNote() {
        items.add(Note(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
        addNoteLiveData.postValue(true)
        notesLiveData.postValue(items)
    }

}