package com.alorma.contactnotes.data.contacts

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import com.github.tamir7.contacts.Contacts
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class AndroidContactsDataSource(private val contentResolver: ContentResolver) : ContactsDataSource {

    companion object {
        val LOOKUP_URI = "content://com.android.contacts/contacts/lookup/"
    }

    override fun getContacts(): Flowable<List<Contact>> {
        return Flowable.fromCallable({
            loadContacts()
        })
    }

    private fun loadContacts(): List<Contact> = Contacts.getQuery().hasPhoneNumber().find().map {
        mapContact(it)
    }

    override fun getContactByRawId(rawId: String): Maybe<Contact> {
        return Maybe.fromCallable {
            val queryId = Contacts.getQuery().whereEqualTo(com.github.tamir7.contacts.Contact.Field.ContactId, rawId)
            val queryPhone = Contacts.getQuery().whereEqualTo(com.github.tamir7.contacts.Contact.Field.PhoneNumber, deNormalizeString(rawId))
            val queryPhoneNormalized = Contacts.getQuery().whereEqualTo(com.github.tamir7.contacts.Contact.Field.PhoneNormalizedNumber, deNormalizeString(rawId))
            Contacts.getQuery().or(listOf(queryId, queryPhone, queryPhoneNormalized)).find()
        }.filter { it.isNotEmpty() }
                .map { it[0] }
                .map { mapContact(it) }
    }

    private fun mapContact(contact: com.github.tamir7.contacts.Contact): Contact {
        val id: String = normalizeString(getPhoneNumber(contact))
        return Contact(id, contact.displayName, contact.photoUri)
    }

    private fun normalizeString(string: String): String {
        return string.toLowerCase().replace(" ", "-")
    }

    private fun deNormalizeString(string: String): String {
        return string.toLowerCase().replace("-", " ")
    }

    private fun getPhoneNumber(contact: com.github.tamir7.contacts.Contact): String {
        val phoneNumber = contact.phoneNumbers
                .filterNot { phoneNumber -> phoneNumber == null }
                .sortedBy { it.type }
                .sortedBy { it.label }[0]

        return if (phoneNumber.normalizedNumber != null) {
            phoneNumber.normalizedNumber
        } else {
            phoneNumber.number
        }
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
            val uri = Uri.parse("content://com.android.contacts/contacts/lookup/$lookup")
            loadContact(uri.toString())
        }
    }
}