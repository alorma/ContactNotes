package com.alorma.contactnotes.data.notes

import android.arch.lifecycle.MutableLiveData
import com.alorma.contactnotes.domain.notes.Note

class SingleNoteLiveData private constructor() : MutableLiveData<Note>() {

    private object Holder {
        val INSTANCE = SingleNoteLiveData()
    }

    companion object {
        val INSTANCE: SingleNoteLiveData by lazy { Holder.INSTANCE }
    }
}