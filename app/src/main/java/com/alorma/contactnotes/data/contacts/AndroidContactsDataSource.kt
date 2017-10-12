package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.github.tamir7.contacts.Contacts
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class AndroidContactsDataSource : ContactsDataSource {
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

    override fun insertContact(contact: Contact): Completable {
        return Completable.never()
    }

}