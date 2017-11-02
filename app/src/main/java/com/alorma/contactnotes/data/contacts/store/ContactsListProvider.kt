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

    fun add(contact: Contact): Either<Throwable, Contact> {
        return try {
            db.insertAll(contact.map {
                mapContact(contact)
            })
            Right(contact)
        } catch (e: Exception) {
            Left(e)
        }
    }

    fun list(): Either<Exception, List<Contact>> {
        return Right(db.all.map {
            mapEntity(it)
        })
    }

    fun get(userId: String): Either<Contact, Exception> {
        return db.findById(userId).map {
            it?.let {
                Left(mapEntity(it))
            } ?: Right(NoSuchElementException())
        }
    }

    private fun mapContact(contact: Contact) =
            ContactEntity(id = UUID.randomUUID().toString(),
                    androidId = contact.androidId,
                    name = contact.name,
                    userEmail = contact.userEmail,
                    userPhone = contact.userPhone)

    private fun mapEntity(it: ContactEntity) =
            Contact(id = it.id, androidId = it.androidId, name = it.name, userEmail = it.userEmail, userPhone = it.userPhone)
}
