package com.alorma.contactnotes.data.contacts.store

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.domain.contacts.Contact

class ContactsListProvider private constructor() {

    private val items = mutableMapOf<String, Contact>()

    private object Holder {
        val INSTANCE = ContactsListProvider()
    }

    companion object {
        val INSTANCE: ContactsListProvider by lazy { Holder.INSTANCE }
    }

    fun add(contact: Contact) {
        items.put(contact.id, contact)
    }

    fun list(): List<Contact> {
        val mutableListOf = mutableListOf<Contact>()
        mutableListOf.addAll(items.values)
        return mutableListOf
    }

    fun get(userId: String): Either<Contact, Exception> {
        val contact = items[userId]
        return if (contact == null) {
            Right(NoSuchElementException())
        } else {
            Left(contact)
        }
    }
}