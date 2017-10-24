package com.alorma.contactnotes.ui.contacts.create;

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.ContentResolver
import com.alorma.contactnotes.data.contacts.AndroidGetContact
import com.alorma.contactnotes.data.contacts.ContactsListProvider
import com.alorma.contactnotes.data.contacts.InsertContact
import com.alorma.contactnotes.domain.validator.*

class CreateContactViewModelFactory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CreateContactViewModel::class.java -> CreateContactViewModel(buildUsernameValidator(),
                    buildEmailValidator(), buildPhoneValidator(), provideInsertContact(), provideAndroidGetContact()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideInsertContact(): InsertContact {
        return InsertContact(ContactsListProvider.INSTANCE)
    }

    private fun provideAndroidGetContact(): AndroidGetContact {
        return AndroidGetContact(contentResolver)
    }

    private fun buildUsernameValidator(): Validator<String, String> = Validator(NotEmptyRule(), MinLengthRule(3), MaxLengthRule(50))

    private fun buildEmailValidator(): Validator<String, String> = Validator(NotEmptyRule(), NoSpacesRule(), MinLengthRule(5), EmailRule())

    private fun buildPhoneValidator(): Validator<String, String> = Validator(NotEmptyRule(), PhoneRule())

}