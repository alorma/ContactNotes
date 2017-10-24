package com.alorma.contactnotes.data.contacts.livedata

import android.arch.lifecycle.MutableLiveData
import com.alorma.contactnotes.domain.contacts.Contact

class ContactsLiveData private constructor() : MutableLiveData<List<Contact>>() {

    private object Holder {
        val INSTANCE = ContactsLiveData()
    }

    companion object {
        val INSTANCE: ContactsLiveData by lazy { Holder.INSTANCE }
    }
}