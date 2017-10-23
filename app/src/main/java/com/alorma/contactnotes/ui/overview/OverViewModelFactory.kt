package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.alorma.contactnotes.data.contacts.AndroidContactsDataSource
import com.alorma.contactnotes.data.contacts.AppContactsDataSource
import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.contacts.ContactsRepository

class OverViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        return when (modelClass) {
            OverviewViewModel::class.java -> OverviewViewModel(provideListContactsWithNotesUseCase(context)) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideListContactsWithNotesUseCase(context: Context): ListContactsWithNotesUseCase {
        return ListContactsWithNotesUseCase(provideContactsRepository(context))
    }

    private fun provideContactsRepository(context: Context): ContactsRepository {
        return ContactsRepository(provideSystemContactsDataSource(context), provideContactsDataSource())
    }

    private fun provideSystemContactsDataSource(context: Context): ContactsDataSource {
        return AndroidContactsDataSource(context.contentResolver)
    }

    private fun provideContactsDataSource(): ContactsDataSource {
        return AppContactsDataSource()
    }
}