package com.alorma.contactnotes.data.contacts

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

    fun get(userId: String): Contact? {
        return items[userId]
    }
}