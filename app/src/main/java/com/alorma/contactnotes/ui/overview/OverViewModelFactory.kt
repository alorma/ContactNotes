package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.contacts.ContactsListProvider
import com.alorma.contactnotes.data.contacts.ListContacts

class OverViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        return when (modelClass) {
            OverviewViewModel::class.java -> OverviewViewModel(provideListContacts()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideListContacts(): ListContacts {
        return ListContacts(ContactsListProvider.INSTANCE)
    }
}