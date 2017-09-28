package com.alorma.contactnotes.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.domain.GetNoteFromContactUseCase
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.ListExternalContactsUseCase
import com.alorma.contactnotes.ui.contacts.ContactsViewModel
import com.alorma.contactnotes.ui.notes.NoteViewModel
import com.alorma.contactnotes.ui.overview.OverviewViewModel

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(private val listExternalContactsUseCase: ListExternalContactsUseCase,
                          private val listContactsWithNotesUseCase: ListContactsWithNotesUseCase,
                          private val insertContactUseCase: InsertContactUseCase,
                          private val getNoteFromContactUseCase: GetNoteFromContactUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ContactsViewModel::class.java -> ContactsViewModel(listExternalContactsUseCase) as T
            OverviewViewModel::class.java -> OverviewViewModel(listContactsWithNotesUseCase, insertContactUseCase) as T
            NoteViewModel::class.java -> NoteViewModel(getNoteFromContactUseCase) as T
            else -> throw IllegalArgumentException()
        }
    }
}