package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase

class OverviewViewModel(private val listContactsWithNotesUseCase: ListContactsWithNotesUseCase) : ViewModel() {

    fun getContacts() = ContactLiveData.INSTANCE

    fun loadContacts() {
        listContactsWithNotesUseCase.execute()
    }
}