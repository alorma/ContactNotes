package com.alorma.contactnotes.data.contacts

import android.content.ContentProviderClient
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class AndroidContactsDataSource(private val context: Context) : ContactsDataSource {
    companion object {
        val QUERY_URI: Uri = ContactsContract.RawContacts.CONTENT_URI
        val RAW_CONTACT_ID = ContactsContract.RawContacts.SOURCE_ID
        val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    }

    override fun getContacts(): Flowable<List<Contact>> {
        return Flowable.fromCallable({
            loadContacts()
        })
    }

    private fun loadContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        getContactsProvider().query(QUERY_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)?.use { cursor ->

            val nameIndex = cursor.getColumnIndex(DISPLAY_NAME)
            val rawIndex = cursor.getColumnIndex(RAW_CONTACT_ID)
            while (cursor.moveToNext()) {
                val rawId = cursor.getString(rawIndex)
                val name = cursor.getString(nameIndex)
                contacts.add(Contact(rawId, name))
            }
        }

        return contacts
    }

    override fun getContactByRawId(rawId: String): Maybe<Contact> {
        return Single.fromCallable {
            getContactsProvider()
        }.map {
            val selectionArgs = arrayOf(rawId)
            it.query(QUERY_URI, null, "$RAW_CONTACT_ID = ?", selectionArgs, null)
        }.filter {
            it.moveToFirst()
        }.map {
            val nameIndex = it.getColumnIndex(DISPLAY_NAME)
            val rawIndex = it.getColumnIndex(RAW_CONTACT_ID)

            val id = it.getString(rawIndex)
            val name = it.getString(nameIndex)

            Contact(id, name)
        }
    }

    private fun getContactsProvider(): ContentProviderClient {
        val contentResolver = context.contentResolver
        return contentResolver.acquireContentProviderClient(QUERY_URI)
    }

    override fun insertContact(contact: Contact): Completable {
        return Completable.never()
    }

}