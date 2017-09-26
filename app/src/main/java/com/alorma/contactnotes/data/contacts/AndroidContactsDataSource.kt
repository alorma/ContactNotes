package com.alorma.contactnotes.data.contacts

import android.content.ContentProviderClient
import android.content.Context
import android.provider.ContactsContract
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class AndroidContactsDataSource(private val context: Context) : ContactsDataSource {
    override fun getContacts(): Flowable<List<Contact>> {
        return Flowable.fromCallable({
            loadContacts()
        })
    }

    private fun loadContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        getContactsProvider().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)?.use { cursor ->

            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val rawIndex = cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID)
            val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI)
            while (cursor.moveToNext()) {
                val rawId = cursor.getString(rawIndex)
                val name = cursor.getString(nameIndex)
                val photo = cursor.getString(photoIndex)
                contacts.add(Contact(rawId, name, photo))
            }
        }

        return contacts
    }

    override fun getContactByRawId(rawId: String): Maybe<Contact> {
        return Single.fromCallable {
            getContactsProvider()
        }.map {
            val selection = ContactsContract.Contacts.NAME_RAW_CONTACT_ID + " = ?"
            val selectionArgs = arrayOf(rawId)
            it.query(ContactsContract.Contacts.CONTENT_URI, null, selection, selectionArgs, null)
        }.filter {
            it.moveToFirst()
        }.map {
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val rawIndex = it.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI)

            val id = it.getString(rawIndex)
            val name = it.getString(nameIndex)
            val photo = it.getString(photoIndex)

            Contact(id, name, photo)
        }
    }

    private fun getContactsProvider(): ContentProviderClient {
        val contentResolver = context.contentResolver
        return contentResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI)
    }

    override fun insertContact(it: Contact): Completable {
        return Completable.never()
    }

}