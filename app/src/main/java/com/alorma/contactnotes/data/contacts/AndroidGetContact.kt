package com.alorma.contactnotes.data.contacts

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import com.alorma.contactnotes.domain.contacts.Contact

class AndroidGetContact(private val contentResolver: ContentResolver) {

    fun loadContact(contactUri: Uri): Contact {
        val cursor = contentResolver.query(contactUri, null,
                null, null, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val lookupIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)

            val id = cursor.getString(idIndex)
            val name = cursor.getString(nameIndex)
            val lookup = cursor.getString(lookupIndex)

            cursor.close()

            val email: String? = loadEmail(id)
            val phone: String? = loadPhone(id)
            val photo: String? = loadPhoto(id)

            return Contact(id, name = name, lookup = lookup, userEmail = email, userPhone = phone, photo = photo)
        } else {
            throw Exception()
        }
    }

    private fun loadEmail(id: String): String? {
        val cur1 = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                arrayOf(id), null, null)
        var email: String? = null
        if (cur1.moveToFirst()) {
            email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
        }

        cur1.close()
        return email
    }

    private fun loadPhone(id: String): String? {
        val cur1 = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id), null, null)
        var phone: String? = null
        if (cur1.moveToFirst()) {
            phone = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA))
        }

        cur1.close()
        return phone
    }

    private fun loadPhoto(id: String): String? {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
        val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        return photoUri.toString()
    }
}