package com.alorma.contactnotes.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import com.alorma.contactnotes.data.AppDatabase
import com.alorma.contactnotes.data.contacts.AndroidContactsDataSource
import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.data.contacts.RoomContactsDataSource
import com.alorma.contactnotes.data.contacts.persistance.ContactsDAO
import com.alorma.contactnotes.data.notes.NotesDataSource
import com.alorma.contactnotes.data.notes.RoomNotesDataSource
import com.alorma.contactnotes.data.notes.persistance.NotesDAO
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.ListExternalContactsUseCase
import com.alorma.contactnotes.domain.notes.NotesRepository

open class Injector {

    protected fun <T : ViewModel> provideViewModel(activity: FragmentActivity,
                                                   modelClass: Class<T>): T {
        return ViewModelProviders.of(activity, provideAppViewModelFactory(activity)).get(modelClass)
    }

    private fun provideAppViewModelFactory(context: Context): ViewModelProvider.Factory {
        return AppViewModelFactory(provideListExternalContactsUseCase(context),
                provideListContactsWithNotesUseCase(context),
                provideInsertContactUseCase(context))
    }

    private fun provideContactsRepository(context: Context): ContactsRepository {
        return ContactsRepository(provideSystemContactsDataSource(context), provideLocalContactsDataSource(context))
    }

    private fun provideSystemContactsDataSource(context: Context): ContactsDataSource {
        return AndroidContactsDataSource(context)
    }

    private fun provideContactsDatabase(context: Context): ContactsDAO {
        return AppDatabase.getInstance(context).contactsDao()
    }

    private fun provideLocalContactsDataSource(context: Context): RoomContactsDataSource {
        return RoomContactsDataSource(provideContactsDatabase(context))
    }

    private fun provideListExternalContactsUseCase(context: Context): ListExternalContactsUseCase {
        return ListExternalContactsUseCase(provideContactsRepository(context))
    }

    private fun provideListContactsWithNotesUseCase(context: Context): ListContactsWithNotesUseCase {
        return ListContactsWithNotesUseCase(provideContactsRepository(context), provideNotesRepository(context))
    }

    private fun provideInsertContactUseCase(context: Context): InsertContactUseCase {
        return InsertContactUseCase(provideContactsRepository(context), provideNotesRepository(context))
    }

    private fun provideNotesRepository(context: Context): NotesRepository {
        return NotesRepository(provideNotesDataSource(context))
    }

    private fun provideNotesDataSource(context: Context): NotesDataSource {
        return RoomNotesDataSource(provideNotesDatabase(context))
    }

    private fun provideNotesDatabase(context: Context): NotesDAO {
        return AppDatabase.getInstance(context).notesDao()
    }

}