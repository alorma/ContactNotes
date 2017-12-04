package com.alorma.contactnotes.data.contacts.operations

import android.app.IntentService
import android.content.Intent
import com.alorma.contactnotes.arch.fold
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.data.notes.store.NotesProvider

class SyncUsers : IntentService("SyncUsers") {

    override fun onHandleIntent(intent: Intent) {

        val contactsListProvider = ContactsListProvider.INSTANCE
        val notesProvider = NotesProvider.INSTANCE
        if (contactsListProvider != null && notesProvider != null) {
            val updateAndroidContact = NoteFieldUpdateAndroidContact(contactsListProvider, notesProvider, contentResolver)
            
            contactsListProvider.list().fold({}, {
                it.forEach {
                    if (it.id != null) {
                        updateAndroidContact.execute(it.id)
                    }
                }
            })
        }


    }
}