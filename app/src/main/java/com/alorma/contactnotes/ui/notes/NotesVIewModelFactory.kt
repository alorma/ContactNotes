package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.notes.FirebaseNotesDataSource
import com.alorma.contactnotes.domain.GetNotesFromContactUseCase
import com.alorma.contactnotes.domain.notes.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class NotesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ListNotesViewModel::class.java -> ListNotesViewModel(buildGetNotesUseCase()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildGetNotesUseCase(): GetNotesFromContactUseCase {
        val notesRepository = NotesRepository(provideFirebaseNotesDataSource())
        return GetNotesFromContactUseCase(notesRepository)
    }

    private fun provideFirebaseNotesDataSource(): FirebaseNotesDataSource {
        return FirebaseNotesDataSource(FirebaseAuth.getInstance(), provideFirebaseStorage())
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