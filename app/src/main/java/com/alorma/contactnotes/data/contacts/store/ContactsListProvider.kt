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
        return contact.copy(id = contactEntity.id)
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

    private fun getByAndroidId(userId: String): ContactEntity? = db.findByAndroidId(userId)

    private fun mapContact(contact: Contact) =
            ContactEntity(id = contact.id ?: UUID.randomUUID().toString(),
                    androidId = contact.androidId,
                    name = contact.name,
                    userEmail = contact.userEmail,
                    userPhone = contact.userPhone)

    private fun mapEntity(it: ContactEntity) =
            Contact(id = it.id, androidId = it.androidId, name = it.name, userEmail = it.userEmail, userPhone = it.userPhone)
}
