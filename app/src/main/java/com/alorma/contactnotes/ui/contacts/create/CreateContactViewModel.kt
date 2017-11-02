package com.alorma.contactnotes.ui.contacts.create

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.alorma.contactnotes.arch.BaseViewModel
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.data.contacts.operations.AndroidGetContact
import com.alorma.contactnotes.data.contacts.operations.InsertContact
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.create.CreateUserForm
import com.alorma.contactnotes.domain.validator.Validator
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class CreateContactViewModel(private val createUserValidator: Validator<CreateUserForm, Exception>,
                             private val insertContact: InsertContact,
                             private val androidGetContact: AndroidGetContact) : BaseViewModel() {

    fun setupCreateContact(lifecycleRelay: Relay<Lifecycle.Event>,
                           contactRelay: Relay<CreateUserForm>,
                           consumer: Consumer<Either<Throwable, Contact>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE)
                .switchMap { contactRelay }
                .observeOn(Schedulers.io())
                .map { createUserValidator.validate(it) }
                .flatMapSingle { insertContact.insert(it) }
                .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }

    fun setupContactImported(lifecycleRelay: Relay<Lifecycle.Event>, uriRelay: Relay<Uri>, consumer: Consumer<Either<Throwable, Contact>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_RESUME)
                .switchMap { uriRelay }
                .observeOn(Schedulers.io())
                .flatMapSingle { androidGetContact.loadContact(it) }
                .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }
}
