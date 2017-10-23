package com.alorma.contactnotes.ui.contacts.create

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.LoadContactUseCase
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import com.alorma.contactnotes.domain.validator.Validator

class CreateContactViewModel(private val usernameValidator: Validator<String, String>,
                             private val emailValidator: Validator<String, String>,
                             private val phoneValidator: Validator<String, String>,
                             private val insertContactUseCase: InsertContactUseCase,
                             private val loadContactUseCase: LoadContactUseCase) : ViewModel() {

    private val resultLiveData = InsertContactLiveData.INSTANCE
    private val importContact = ContactLiveData.INSTANCE

    fun getUsernameValidationError() = usernameValidator.getReason()
    fun getEmailValidationError() = emailValidator.getReason()
    fun getPhoneValidationError() = phoneValidator.getReason()

    fun create(userName: String, userEmail: String, userPhone: String): LiveData<Boolean> {
        if (usernameValidator.validate(userName)
                && (userEmail.isEmpty() || emailValidator.validate(userEmail))
                && (userPhone.isEmpty() || phoneValidator.validate(userPhone))) {

            insertContactUseCase.execute(CreateUserForm(userName, userEmail, userPhone, importContact.value?.lookup))
        }
        return resultLiveData
    }

    fun contactImported(contactUri: Uri): LiveData<Contact> {
        loadContactUseCase.execute(contactUri.toString())
        return importContact
    }
}