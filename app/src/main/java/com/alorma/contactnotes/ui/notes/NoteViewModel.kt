package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.GetNoteFromContactUseCase
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.viewmodel.ConsumableEitherLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoteViewModel(private val getNoteFromContactUseCase: GetNoteFromContactUseCase) : ViewModel() {

    private val noteLiveData = ConsumableEitherLiveData<Note>()

    fun getNote() = noteLiveData

    fun loadData(contactId: String) {
        getNoteFromContactUseCase.getNote(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteLiveData)
    }
}