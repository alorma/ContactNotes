package com.alorma.contactnotes.data.contacts.operations

import com.alorma.contactnotes.arch.*
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.create.CreateUserForm
import io.reactivex.Single
import java.util.*

class InsertContact(private val contactsProvider: ContactsListProvider?) {

    fun insert(createUserForm: Either<Throwable, CreateUserForm>): Either<Throwable, Contact> {
        return createUserForm.flatMap {
            val contact = Contact(UUID.randomUUID().toString(),
                    androidId = it.androidId ?: "",
                    name = it.userName,
                    userPhone = it.userPhone,
                    userEmail = it.userEmail)
            contactsProvider?.add(contact) ?: Left(NullPointerException("ContactsListProvider is null"))
        }
    }
}