package com.alorma.contactnotes.ui.contacts.create

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.alorma.contactnotes.data.contacts.operations.AndroidGetContact
import com.alorma.contactnotes.data.contacts.operations.InsertContact
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.create.CreateUserForm
import com.alorma.contactnotes.domain.validator.Validator

class CreateContactViewModel(private val usernameValidator: Validator<String, String>,
                             private val emailValidator: Validator<String, String>,
                             private val phoneValidator: Validator<String, String>,
                             private val insertContact: InsertContact,
                             private val androidGetContact: AndroidGetContact) : ViewModel() {

    private val importContact = MutableLiveData<Contact>()
    private val createContact = MutableLiveData<Boolean>()

    fun getUsernameValidationError() = usernameValidator.getReason()
    fun getEmailValidationError() = emailValidator.getReason()
    fun getPhoneValidationError() = phoneValidator.getReason()

    fun create(userName: String, userEmail: String, userPhone: String): LiveData<Boolean> {
        if (usernameValidator.validate(userName)
                && (userEmail.isEmpty() || emailValidator.validate(userEmail))
                && (userPhone.isEmpty() || phoneValidator.validate(userPhone))) {

            val createUserForm = CreateUserForm(userName = userName,
                    userEmail = userEmail,
                    userPhone = userPhone,
                    androidId = importContact.value?.androidId,
                    lookup = importContact.value?.lookup)
            insertContact.insert(createUserForm)
            createContact.value = true
        }
        return createContact
    }

    fun contactImported(contactUri: Uri): LiveData<Contact> {
        val contact = androidGetContact.loadContact(contactUri)
        importContact.postValue(contact)
        return importContact
    }
}