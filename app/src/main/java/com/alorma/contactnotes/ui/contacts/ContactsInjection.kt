package com.alorma.contactnotes.ui.contacts

import android.support.v4.app.FragmentActivity
import com.alorma.contactnotes.ui.Injector

object ContactsInjection : Injector() {
    fun provideContactsViewModel(activity: FragmentActivity): ContactsViewModel {
        return provideViewModel(activity, ContactsViewModel::class.java)
    }
}