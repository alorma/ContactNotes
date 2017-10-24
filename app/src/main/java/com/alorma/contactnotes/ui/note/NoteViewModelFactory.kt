package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.domain.validator.NotEmptyRule
import com.alorma.contactnotes.domain.validator.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class NoteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NoteViewModel::class.java -> NoteViewModel(buildNoteValidator()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildNoteValidator() = Validator(NotEmptyRule())

}