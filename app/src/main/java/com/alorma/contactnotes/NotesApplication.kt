package com.alorma.contactnotes

import android.app.Application
import com.github.tamir7.contacts.Contacts
import com.google.firebase.FirebaseApp

class NotesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Contacts.initialize(this);
    }
}