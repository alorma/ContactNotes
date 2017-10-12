package com.alorma.contactnotes.ui.contacts.create;

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alorma.contactnotes.data.contacts.AndroidContactsDataSource
import com.alorma.contactnotes.data.contacts.FirebaseStorageContactsDataSource
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.validator.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class CreateContactViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CreateContactViewModel::class.java -> CreateContactViewModel(buildUsernameValidator(),
                    buildEmailValidator(), buildPhoneValidator(), createInsertContactUseCase()) as T
            else -> throw IllegalArgumentException()
        }
    }

    private fun provideContactsDataSource(): FirebaseStorageContactsDataSource {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = settings
        return FirebaseStorageContactsDataSource(FirebaseAuth.getInstance(), db)
    }

    private fun createInsertContactUseCase(): InsertContactUseCase {
        val system = AndroidContactsDataSource()
        val remote = provideContactsDataSource()
        val contactsRepository = ContactsRepository(system, remote)
        return InsertContactUseCase(contactsRepository)
    }

    private fun buildUsernameValidator(): Validator<String, String> = Validator(NotEmptyRule(), NoSpacesRule(), MinLengthRule(3), MaxLengthRule(50))

    private fun buildEmailValidator(): Validator<String, String> = Validator(NotEmptyRule(), NoSpacesRule(), MinLengthRule(5), EmailRule())

    private fun buildPhoneValidator(): Validator<String, String> = Validator(NotEmptyRule(), PhoneRule())

}