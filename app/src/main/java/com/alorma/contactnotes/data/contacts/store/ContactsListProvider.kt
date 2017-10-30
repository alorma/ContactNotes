package com.alorma.contactnotes.data.contacts.store

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.map
import java.util.*

class ContactsListProvider private constructor(private val db: ContactDao) {

    companion object {
        var INSTANCE: ContactsListProvider? = null

        fun init(db: ContactDao) {
            INSTANCE = ContactsListProvider(db)
        }
    }

    fun add(contact: Contact) {
        db.insertAll(contact.map {
            mapContact(contact)
        })
    }

    fun list(): List<Contact> {
        return db.all.map {
            mapEntity(it)
        }
    }

    fun get(userId: String): Either<Contact, Exception> {
        return db.findById(userId).map {
            it?.let {
                Left(mapEntity(it))
            } ?: Right(NoSuchElementException())
        }
    }

    private fun mapContact(contact: Contact) =
            ContactEntity(UUID.randomUUID().toString(), contact.androidId, contact.name, contact.userEmail, contact.userPhone)

    private fun mapEntity(it: ContactEntity) =
            Contact(androidId = it.id, name = it.name, userEmail = it.userEmail, userPhone = it.userPhone)
}
