package com.alorma.contactnotes.ui.contacts.create

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.LoadContactUseCase
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import com.alorma.contactnotes.domain.validator.Validator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateContactViewModel(private val usernameValidator: Validator<String, String>,
                             private val emailValidator: Validator<String, String>,
                             private val phoneValidator: Validator<String, String>,
                             private val insertContactUseCase: InsertContactUseCase,
                             private val loadContactUseCase: LoadContactUseCase) : ViewModel() {

    private val resultLiveData = MutableLiveData<Boolean>()
    private val importContact = MutableLiveData<Contact>()

    fun getResult(): LiveData<Boolean> = resultLiveData

    fun getUsernameValidationError() = usernameValidator.getReason()
    fun getEmailValidationError() = emailValidator.getReason()
    fun getPhoneValidationError() = phoneValidator.getReason()

    fun create(userName: String, userEmail: String, userPhone: String) {
        if (usernameValidator.validate(userName)
                && (userEmail.isEmpty() || emailValidator.validate(userEmail))
                && (userPhone.isEmpty() || phoneValidator.validate(userPhone))) {

            insertContactUseCase.execute(CreateUserForm(userName, userEmail, userPhone, importContact.value?.lookup))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        resultLiveData.postValue(true)
                    }, {
                        resultLiveData.postValue(false)
                    })
        }
    }

    fun contactImported(contactUri: Uri): LiveData<Contact> {
        loadContactUseCase.execute(contactUri.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onContactLoaded(it)
                }, {})
        return importContact
    }

    private fun onContactLoaded(contact: Contact) {
        importContact.postValue(contact)
    }

}