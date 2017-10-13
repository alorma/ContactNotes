package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.GetNotesFromContactUseCase
import com.alorma.contactnotes.domain.notes.Note
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListNotesViewModel(private val getNotesUseCase: GetNotesFromContactUseCase) : ViewModel() {

    private val notesLiveData = MutableLiveData<List<Note>>()

    fun getData(): LiveData<List<Note>> = notesLiveData

    private lateinit var contactId: String

    fun load(contactId: String) {
        this.contactId = contactId
        getNotesUseCase.execute(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    notesLiveData.postValue(it)
                }, {
                    Crashlytics.getInstance().core.logException(it)
                })
    }
}