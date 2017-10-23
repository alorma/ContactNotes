package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import java.util.*

class InsertContact(private val contactsProvider: ContactsListProvider) {

    fun insert(createUserForm: CreateUserForm): Contact {
        val contact = Contact(UUID.randomUUID().toString(),
                androidId = createUserForm.androidId,
                name = createUserForm.userName,
                userPhone = createUserForm.userPhone,
                userEmail = createUserForm.userEmail)

        contactsProvider.add(contact)
        return contact
    }

}