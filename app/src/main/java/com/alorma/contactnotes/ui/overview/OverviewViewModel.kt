package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.contacts.operations.ListContacts
import com.alorma.contactnotes.data.notes.ListContactNotes
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OverviewViewModel(private val listContacts: ListContacts,
                        private val listContactNotes: ListContactNotes) : ViewModel() {

    val contactsLiveData = MutableLiveData<List<Contact>>()

    fun loadContacts() {
        listContacts.list()
                .flatMapIterable { it }
                .map {
                    val notes = listContactNotes.list(it.id)
                    it.copy(notes = notes)
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ contactsLiveData.value = it }, {})
    }
}