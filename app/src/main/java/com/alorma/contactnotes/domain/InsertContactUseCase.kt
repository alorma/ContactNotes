package com.alorma.contactnotes.domain;

import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.create.CreateUserForm

class InsertContactUseCase(private val contactsRepository: ContactsRepository) {

    fun execute(userForm: CreateUserForm) {
        return contactsRepository.insert(userForm)
    }

}
