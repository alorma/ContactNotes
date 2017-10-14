package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.alorma.contactnotes.data.contacts.AndroidContactsDataSource
import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.data.contacts.FirebaseContactsDataSource
import com.alorma.contactnotes.data.notes.FirebaseNotesDataSource
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.contacts.ContactRepository
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.notes.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class OverViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        return when (modelClass) {
            OverviewViewModel::class.java -> OverviewViewModel(provideListContactsWithNotesUseCase(context)) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideContactsRepository(context: Context): ContactsRepository {
        return ContactsRepository(provideSystemContactsDataSource(context), provideContactsDataSource())
    }

    private fun provideListContactsWithNotesUseCase(context: Context): ListContactsWithNotesUseCase {
        return ListContactsWithNotesUseCase(provideContactsRepository(context), provideNotesRepository(), provideContactRepository(context))
    }

    private fun provideContactRepository(context: Context): ContactRepository {
        return ContactRepository(provideSystemContactsDataSource(context))
    }

    private fun provideNotesRepository(): NotesRepository {
        return NotesRepository(provideFirebaseNotesDataSource())
    }

    private fun provideFirebaseNotesDataSource(): FirebaseNotesDataSource {
        return FirebaseNotesDataSource(FirebaseAuth.getInstance(), provideFirebaseStorage())
    }

    private fun provideSystemContactsDataSource(context: Context): ContactsDataSource {
        return AndroidContactsDataSource(context.contentResolver)
    }

    private fun provideContactsDataSource(): FirebaseContactsDataSource {
        return FirebaseContactsDataSource(FirebaseAuth.getInstance(), provideFirebaseStorage())
    }

    private fun provideFirebaseStorage(): FirebaseFirestore {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = settings
        return db
    }
}