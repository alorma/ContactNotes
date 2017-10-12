package com.alorma.contactnotes.domain;

import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.create.CreateUserForm
import io.reactivex.Completable

class InsertContactUseCase(private val contactsRepository: ContactsRepository) {

    fun execute(userForm: CreateUserForm): Completable {
        return contactsRepository.insert(userForm)
    }

}
