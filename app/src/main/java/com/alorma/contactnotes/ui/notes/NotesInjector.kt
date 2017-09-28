package com.alorma.contactnotes.ui.notes

import android.support.v4.app.FragmentActivity
import com.alorma.contactnotes.ui.Injector
import com.alorma.contactnotes.ui.overview.OverviewViewModel

object NotesInjector : Injector() {
    fun provideOverviewViewModel(activity: FragmentActivity): NoteViewModel {
        return provideViewModel(activity, NoteViewModel::class.java)
    }
}