package com.alorma.contactnotes.data.notes

import android.arch.lifecycle.MutableLiveData
import com.alorma.contactnotes.domain.notes.Note

class InsertNoteLiveData private constructor() : MutableLiveData<String>() {

    private object Holder {
        val INSTANCE = InsertNoteLiveData()
    }

    companion object {
        val INSTANCE: InsertNoteLiveData by lazy { Holder.INSTANCE }
    }
}