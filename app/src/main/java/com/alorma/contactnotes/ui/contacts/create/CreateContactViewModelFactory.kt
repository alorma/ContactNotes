package com.alorma.contactnotes.ui.contacts.create;

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.domain.validator.*

class CreateContactViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CreateContactViewModel::class.java -> CreateContactViewModel(buildUsernameValidator(),
                    buildEmailValidator(), buildPhoneValidator()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun buildUsernameValidator(): Validator<String, String> = Validator(NotEmptyRule(), NoSpacesRule(), MinLengthRule(3), MaxLengthRule(50))

    private fun buildEmailValidator(): Validator<String, String> = Validator(NotEmptyRule(), NoSpacesRule(), MinLengthRule(5), EmailRule())

    private fun buildPhoneValidator(): Validator<String, String> = Validator(NotEmptyRule(), PhoneRule())

}