package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.Lifecycle
import com.alorma.contactnotes.arch.BaseViewModel
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.map
import com.alorma.contactnotes.data.contacts.operations.ListContacts
import com.alorma.contactnotes.data.notes.ListContactNotes
import com.alorma.contactnotes.domain.contacts.Contact
import com.jakewharton.rxrelay2.Relay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class OverviewViewModel(private val listContacts: ListContacts,
                        private val listContactNotes: ListContactNotes) : BaseViewModel() {

    fun subscribeLoadContacts(lifecycleRelay: Relay<Lifecycle.Event>,
                              consumer: Consumer<Either<Throwable, List<Contact>>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE)
                .observeOn(Schedulers.io())
                .flatMap { listContacts.list() }
                .map {
                    it.map {
                        it.map {
                            val notes = listContactNotes.list(it.id)
                            it.copy(notes = notes)
                        }
                    }
                }
                .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }
}