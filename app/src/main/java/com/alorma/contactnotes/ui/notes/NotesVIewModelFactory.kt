package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class NotesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ListNotesViewModel::class.java -> ListNotesViewModel() as T
            else -> throw IllegalArgumentException()
        }
    }

}