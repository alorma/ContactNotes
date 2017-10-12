package com.alorma.contactnotes.ui.contacts.pick

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alorma.contactnotes.domain.ListExternalContactsUseCase
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ContactsViewModel(private val listExternalContactsUseCase: ListExternalContactsUseCase) : ViewModel() {

    val contacts = MutableLiveData<List<Contact>>()
    private val disposable: CompositeDisposable = CompositeDisposable()
    val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    fun loadContacts() {
        val dispose = listExternalContactsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ contacts.postValue(it) }, { errorLiveData.postValue(it) })

        disposable.add(dispose)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}
