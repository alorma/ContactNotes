package com.alorma.contactnotes.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import com.alorma.contactnotes.data.contacts.AndroidContactsDataSource
import com.alorma.contactnotes.data.contacts.ContactsDataSource
import com.alorma.contactnotes.data.contacts.FirebaseStorageContactsDataSource
import com.alorma.contactnotes.data.notes.FirebaseNotesDataSource
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.ListExternalContactsUseCase
import com.alorma.contactnotes.domain.notes.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

open class Injector {

    protected fun <T : ViewModel> provideViewModel(activity: FragmentActivity,
                                                   modelClass: Class<T>): T {
        return ViewModelProviders.of(activity, provideAppViewModelFactory(activity)).get(modelClass)
    }

    private fun provideAppViewModelFactory(context: Context): ViewModelProvider.Factory {
        return AppViewModelFactory(provideListExternalContactsUseCase(context),
                provideListContactsWithNotesUseCase(context)
        )
    }

    private fun provideContactsRepository(): ContactsRepository {
        return ContactsRepository(provideSystemContactsDataSource(), provideContactsDataSource())
    }

    private fun provideSystemContactsDataSource(): ContactsDataSource {
        return AndroidContactsDataSource()
    }

    private fun provideContactsDataSource(): FirebaseStorageContactsDataSource {
        return FirebaseStorageContactsDataSource(FirebaseAuth.getInstance(), provideFirebaseStorage())
    }

    private fun provideFirebaseStorage(): FirebaseFirestore {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = settings
        return db
    }

    private fun provideListExternalContactsUseCase(context: Context): ListExternalContactsUseCase {
        return ListExternalContactsUseCase(provideContactsRepository())
    }

    private fun provideListContactsWithNotesUseCase(context: Context): ListContactsWithNotesUseCase {
        return ListContactsWithNotesUseCase(provideContactsRepository(), provideNotesRepository())
    }

    private fun provideNotesRepository(): NotesRepository {
        return NotesRepository(provideFirebaseNotesDataSource())
    }

    private fun provideFirebaseNotesDataSource(): FirebaseNotesDataSource {
        return FirebaseNotesDataSource(FirebaseAuth.getInstance(), provideFirebaseStorage())
    }
}