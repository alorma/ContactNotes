package com.alorma.contactnotes.data.contacts

import android.arch.lifecycle.MutableLiveData

class ErrorContactLiveData private constructor() : MutableLiveData<Exception>() {

    private object Holder {
        val INSTANCE = ErrorContactLiveData()
    }

    companion object {
        val INSTANCE: ErrorContactLiveData by lazy { Holder.INSTANCE }
    }
}