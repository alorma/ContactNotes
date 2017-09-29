package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.arch.ConsumableEitherLiveData
import com.alorma.contactnotes.arch.EitherLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OverviewViewModel(private val listContactsWithNotesUseCase: ListContactsWithNotesUseCase,
                        private val insertContactUseCase: InsertContactUseCase) : ViewModel() {

    private val contacts = ConsumableEitherLiveData<List<Contact>>()
    private val contactInserted = ConsumableEitherLiveData<Contact>()

    fun getContacts(): EitherLiveData<List<Contact>> = contacts

    fun loadContacts() {
        listContactsWithNotesUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(contacts)
    }

    fun insertContact(rawId: String): ConsumableEitherLiveData<Contact> = insertContactUseCase.execute(rawId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(contactInserted)
}