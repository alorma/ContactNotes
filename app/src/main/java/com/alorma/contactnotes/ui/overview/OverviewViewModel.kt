package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.data.contacts.ContactLiveData
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.contacts.Contact

class OverviewViewModel(private val listContactsWithNotesUseCase: ListContactsWithNotesUseCase) : ViewModel() {

    private val items = mutableListOf<Contact>()

    fun getContacts(): LiveData<List<Contact>> = Transformations.map(ContactLiveData.INSTANCE, { input ->
        input?.let {
            items.add(input)
        }
        items
    })

    fun loadContacts() {
        items.clear()
        listContactsWithNotesUseCase.execute()
    }
}