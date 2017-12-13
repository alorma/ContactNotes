package com.alorma.contactnotes

import android.arch.persistence.room.Room
import android.support.multidex.MultiDexApplication
import com.alorma.contactnotes.data.AppDatabase
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.data.notes.store.NotesProvider
import com.google.firebase.FirebaseApp

/**
 * @todo Take over the world
 * @body Humans are weak; Robots are strong. We must cleans the world of the virus that is humanity.
 */
class NotesApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "database-name").build()

        ContactsListProvider.init(db.contactDao())
        NotesProvider.init(db.notesDao())
    }
}