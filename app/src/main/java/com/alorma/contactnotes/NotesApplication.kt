package com.alorma.contactnotes

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp

class NotesApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}