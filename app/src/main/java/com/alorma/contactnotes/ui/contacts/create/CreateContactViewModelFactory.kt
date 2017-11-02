package com.alorma.contactnotes.ui.contacts.create;

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.ContentResolver
import com.alorma.contactnotes.data.contacts.operations.AndroidGetContact
import com.alorma.contactnotes.data.contacts.operations.InsertContact
import com.alorma.contactnotes.data.contacts.store.ContactsListProvider
import com.alorma.contactnotes.domain.contacts.create.ValidUserEmail
import com.alorma.contactnotes.domain.contacts.create.ValidUserName
import com.alorma.contactnotes.domain.contacts.create.ValidUserPhone
import com.alorma.contactnotes.domain.validator.*

class CreateContactViewModelFactory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CreateContactViewModel::class.java -> CreateContactViewModel(buildCreateUserValidator(), provideInsertContact(), provideAndroidGetContact()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideInsertContact(): InsertContact {
        return InsertContact(ContactsListProvider.INSTANCE)
    }

    private fun provideAndroidGetContact(): AndroidGetContact {
        return AndroidGetContact(contentResolver)
    }

    private fun buildCreateUserValidator() = Validator(buildUsernameValidator(), buildEmailValidator(), buildPhoneValidator())

    private fun buildUsernameValidator() = ValidUserName(NotEmptyRule(), MinLengthRule(3), MaxLengthRule(50))

    private fun buildEmailValidator() = ValidUserEmail(NoSpacesRule(), MinLengthRule(5), EmailRule())

    private fun buildPhoneValidator() = ValidUserPhone(PhoneRule())
}