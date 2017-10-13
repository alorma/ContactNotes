package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.notes.FirebaseNotesDataSource
import com.alorma.contactnotes.domain.InsertNoteUseCase
import com.alorma.contactnotes.domain.UpdateNoteUseCase
import com.alorma.contactnotes.domain.notes.NotesRepository
import com.alorma.contactnotes.domain.validator.NotEmptyRule
import com.alorma.contactnotes.domain.validator.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class NoteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NoteViewModel::class.java -> NoteViewModel(buildInsertNoteUseCase(),
                    buildUpdateNoteUseCase(), buildNoteValidator()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildInsertNoteUseCase(): InsertNoteUseCase {
        return InsertNoteUseCase(buildNotesRepository())
    }

    private fun buildUpdateNoteUseCase(): UpdateNoteUseCase {
        return UpdateNoteUseCase(buildNotesRepository())
    }

    private fun buildNotesRepository() = NotesRepository(provideFirebaseNotesDataSource())

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

    private fun buildNoteValidator() = Validator(NotEmptyRule())

}