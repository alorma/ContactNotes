package com.alorma.contactnotes.data.contacts.store

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.domain.contacts.Contact

class ContactsListProvider private constructor(private val db: ContactDao) {

    companion object {
        var INSTANCE: ContactsListProvider? = null

        fun init(db: ContactDao) {
            INSTANCE = ContactsListProvider(db)
        }
    }

    fun add(contact: Contact): Either<Throwable, Contact> {
        return try {
            when {
                contact.androidId == null -> Right(insert(contact))
                else -> {
                    val contactEntity = getByAndroidId(contact.androidId)
                    when (contactEntity) {
                        null -> Right(insert(contact))
                        else -> Right(update(contactEntity))
                    }
                }
            }
        } catch (e: Exception) {
            Left(e)
        }
    }

    private fun insert(contact: Contact): Contact {
        val contactEntity = mapContact(contact)
        db.insertAll(contactEntity)
        return contact.copy(id = contactEntity.id.toString())
    }

    private fun update(contactEntity: ContactEntity): Contact {
        db.update(contactEntity)
        return mapEntity(contactEntity)
    }

    fun list(): Either<Throwable, List<Contact>> {
        return Right(db.all.map {
            mapEntity(it)
        })
    }

    fun getById(userId: String): Contact = mapEntity(db.findById(userId))

    private fun getByAndroidId(userId: String): ContactEntity? = db.findByAndroidId(userId)

    private fun mapContact(contact: Contact) =
            ContactEntity(id = contact.id?.toLong() ?: 0,
                    androidId = contact.androidId,
                    name = contact.name,
                    userEmail = contact.userEmail,
                    userPhone = contact.userPhone)

    private fun mapEntity(it: ContactEntity) =
            Contact(it.id.toString(), it.androidId, it.name, it.userEmail, it.userPhone)
}
