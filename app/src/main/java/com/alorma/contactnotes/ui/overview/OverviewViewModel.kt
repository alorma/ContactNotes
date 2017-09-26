package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.ContactsRepository
import com.alorma.contactnotes.domain.InsertContactUseCase
import com.alorma.contactnotes.domain.ListContactsWithNotesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OverviewViewModel(private val listContactsWithNotesUseCase: ListContactsWithNotesUseCase,
                        private val insertContactUseCase: InsertContactUseCase) : ViewModel() {

    val contacts = MutableLiveData<List<Contact>>()
    val contactInserted = MutableLiveData<Contact>()
    val error = MutableLiveData<Throwable>()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun loadContacts() {
        val dispose = listContactsWithNotesUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    contacts.postValue(it)
                }, {
                    error.postValue(it)
                })

        disposable.add(dispose)
    }

    fun insertContact(rawId: String) {
        insertContactUseCase.execute(rawId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ contactInserted.postValue(it) }, { error.postValue(it) })
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}