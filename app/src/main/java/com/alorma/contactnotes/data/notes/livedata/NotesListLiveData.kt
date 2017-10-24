package com.alorma.contactnotes.data.notes.livedata

import android.arch.lifecycle.MutableLiveData
import com.alorma.contactnotes.domain.notes.Note

class NotesListLiveData private constructor() : MutableLiveData<List<Note>>() {

    private object Holder {
        val INSTANCE = NotesListLiveData()
    }

    companion object {
        val INSTANCE: NotesListLiveData by lazy { Holder.INSTANCE }
    }
}