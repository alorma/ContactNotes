package com.alorma.contactnotes.ui.contacts.create;

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.ContentResolver
import com.alorma.contactnotes.data.contacts.AndroidContactsDataSource
import com.alorma.contactnotes.data.contacts.AppContactsDataSource
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.LoadContactUseCase
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.validator.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class CreateContactViewModelFactory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CreateContactViewModel::class.java -> CreateContactViewModel(buildUsernameValidator(),
                    buildEmailValidator(), buildPhoneValidator(), createInsertContactUseCase(),
                    buildLoadContactUseCase()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun createInsertContactUseCase(): InsertContactUseCase {
        return InsertContactUseCase(buildContactsRepository())
    }

    private fun buildContactsRepository(): ContactsRepository {
        val system = AndroidContactsDataSource(contentResolver)
        val remote = AppContactsDataSource()
        return ContactsRepository(system, remote)
    }

    private fun buildLoadContactUseCase(): LoadContactUseCase {
        return LoadContactUseCase(buildContactsRepository())
    }

    private fun buildUsernameValidator(): Validator<String, String> = Validator(NotEmptyRule(), MinLengthRule(3), MaxLengthRule(50))

    private fun buildEmailValidator(): Validator<String, String> = Validator(NotEmptyRule(), NoSpacesRule(), MinLengthRule(5), EmailRule())

    private fun buildPhoneValidator(): Validator<String, String> = Validator(NotEmptyRule(), PhoneRule())

}