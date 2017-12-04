package com.alorma.contactnotes.data.contacts.operations

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.ContactsContract
import android.util.Log
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.data.notes.store.NotesProvider


class NoteFieldUpdateAndroidContact(private val contactsListProvider: ContactsListProvider,
                                    private val notesProvider: NotesProvider,
                                    private val contentResolver: ContentResolver) {

    companion object {
        private val MIME_TYPE_NOTES = "vnd.android.cursor.item/contact_notes"
    }

    fun execute(userId: String) {
        try {
            executeSafe(userId)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun executeSafe(userId: String) {
        contactsListProvider.getById(userId).androidId?.let {
            val count = notesProvider.count(userId)
            when (count) {
                0 -> deleteNotesFieldOnUser(it)
                1 -> updateNoteFieldOnUser(it)
                else -> updateNotesFieldOnUser(it, count)
            }
        }
    }

    private fun deleteNotesFieldOnUser(androidId: String) {
        contentResolver.delete(ContactsContract.Data.CONTENT_URI, getWhere(androidId), null)
    }

    private fun updateNoteFieldOnUser(androidId: String) {
        update(androidId, ContentValues().apply {
            put(ContactsContract.RawContacts.Data.DATA1, "User note")
        })
    }

    private fun updateNotesFieldOnUser(androidId: String, count: Int) {
        update(androidId, ContentValues().apply {
            put(ContactsContract.RawContacts.Data.DATA1, "User notes [$count]")
        })
    }

    private fun update(androidId: String, contentValues: ContentValues) {
        val results = contentResolver.update(
                ContactsContract.Data.CONTENT_URI,
                contentValues,
                getWhere(androidId), null)

        Log.i("NotesProvider", "Where: ${getWhere(androidId)}")
        if (results == 0) {
            contentValues.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, androidId)
            contentValues.put(ContactsContract.RawContacts.Data.MIMETYPE, MIME_TYPE_NOTES)
            val insert = contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
            if (insert != null) {
                Log.i("NotesProvider", "Inserted row: $insert")
            } else {
                Log.i("NotesProvider", "Inserted row: none")
            }
        } else {
            Log.i("NotesProvider", "Updated rows: $results")
        }
    }

    private fun getWhere(androidId: String) =
            ContactsContract.RawContacts.Data.RAW_CONTACT_ID + "='$androidId' AND ${ContactsContract.RawContacts.Data.MIMETYPE}= '$MIME_TYPE_NOTES'"


}