package com.alorma.contactnotes.data.contacts.operations

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Single

class AndroidGetContact(private val contentResolver: ContentResolver) {

    fun loadContact(contactUri: Uri): Single<Either<Throwable, Contact>> {
        return Single.fromCallable {
            try {
                Right(getContact(contactUri))
            } catch (noElement: NoSuchElementException) {
                Left(noElement)
            }
        }
    }

    private fun getContact(contactUri: Uri): Contact {
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

            return Contact(androidId = id,
                    name = name,
                    lookup = lookup,
                    userEmail = email,
                    userPhone = phone)
        } else {
            throw NoSuchElementException()
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
}