package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable
import java.util.*

class AppContactsDataSource : ContactsDataSource {
    override fun loadContact(contactUri: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val items = mutableListOf<Contact>()

    override fun getContacts() {
        ContactsLiveData.INSTANCE.value = items
    }

    override fun insertContact(createUserForm: CreateUserForm): Contact {
        val contact = Contact(UUID.randomUUID().toString(),
                androidId = createUserForm.androidId,
                name = createUserForm.userName,
                userPhone = createUserForm.userPhone,
                userEmail = createUserForm.userEmail)

        items.add(contact)
        return contact
    }

    override fun update(id: String, createUserForm: CreateUserForm): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
