package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable

interface ContactsDataSource {
    fun getContacts()

    fun insertContact(createUserForm: CreateUserForm): Contact

    fun update(id: String, createUserForm: CreateUserForm): Completable

    fun loadContact(contactUri: String)
}