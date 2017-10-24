package com.alorma.contactnotes.data.contacts.livedata

import android.arch.lifecycle.MutableLiveData
import com.alorma.contactnotes.domain.contacts.Contact

class ContactLiveData private constructor() : MutableLiveData<Contact>() {

    private object Holder {
        val INSTANCE = ContactLiveData()
    }

    companion object {
        val INSTANCE: ContactLiveData by lazy { Holder.INSTANCE }
    }
}