package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.InsertNoteUseCase
import com.alorma.contactnotes.domain.UpdateNoteUseCase
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.validator.Validator
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoteViewModel(private val insertNoteUseCase: InsertNoteUseCase,
                    private val updateNoteUseCase: UpdateNoteUseCase, val noteValidator: Validator<String, String>) : ViewModel() {

    private val addNoteLiveData = MutableLiveData<String>()
    private val noteLiveData = MutableLiveData<Note>()

    fun getData(): LiveData<Note> = noteLiveData

    private var newNote = false

    fun newNote() {
        newNote = true
    }

    private lateinit var noteId: String

    fun loadNote(noteId: String) {
        this.noteId = noteId
        newNote = false
    }

    fun saveNote(contactId: String, text: String): LiveData<String> {
        if (noteValidator.validate(text)) {
            if (newNote) {
                insertNoteUseCase.execute(contactId, text)
            } else {
                updateNoteUseCase.execute(contactId, noteId, text)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        addNoteLiveData.postValue(it)
                    }, {
                        Crashlytics.getInstance().core.logException(it)
                    })
        }
        return addNoteLiveData
    }
}
