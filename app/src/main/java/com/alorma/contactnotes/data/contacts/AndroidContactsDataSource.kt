package com.alorma.contactnotes.data.contacts

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class AndroidContactsDataSource(private val contentResolver: ContentResolver) : ContactsDataSource {

    companion object {
        val LOOKUP_URI = "content://com.android.contacts/contacts/lookup/"
    }

    override fun getContacts(): Flowable<List<Contact>> {
        return Flowable.empty()
    }

    override fun getContactByRawId(rawId: String): Maybe<Contact> {
        return Maybe.never()
    }

    override fun insertContact(createUserForm: CreateUserForm): Completable = Completable.never()

    private fun loadContact(contactUri: String): Single<Contact> {
        return Single.fromCallable({

            val cursor = contentResolver.query(Uri.parse(contactUri),
                    null, null, null, null)

            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val lookupIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)

                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val lookup = cursor.getString(lookupIndex)

                cursor.close()

                Contact(id, name, lookup = lookup)
            } else {
                throw Exception()
            }
        })
    }

    override fun getLookupKey(contactUri: String): Single<String> {
        return loadContact(contactUri).map { it.lookup }
    }

    override fun loadContactByLookup(lookup: String): Single<Contact> {
        return Single.defer {
            val uri = Uri.parse("$LOOKUP_URI$lookup")
            loadContact(uri.toString())
        }
    }
}