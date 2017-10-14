package com.alorma.contactnotes.data.notes

import android.arch.lifecycle.MutableLiveData

class ErrorNotesLiveData private constructor() : MutableLiveData<Exception>() {

    private object Holder {
        val INSTANCE = ErrorNotesLiveData()
    }

    companion object {
        val INSTANCE: ErrorNotesLiveData by lazy { Holder.INSTANCE }
    }
}