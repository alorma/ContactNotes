package com.alorma.contactnotes.data.notes.livedata

import android.arch.lifecycle.MutableLiveData

class InsertNoteLiveData private constructor() : MutableLiveData<String>() {

    private object Holder {
        val INSTANCE = InsertNoteLiveData()
    }

    companion object {
        val INSTANCE: InsertNoteLiveData by lazy { Holder.INSTANCE }
    }
}