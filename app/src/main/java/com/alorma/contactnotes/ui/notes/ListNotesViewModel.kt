package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.Lifecycle
import com.alorma.contactnotes.arch.BaseViewModel
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.data.notes.operations.AddContactNote
import com.alorma.contactnotes.data.notes.operations.ListContactNotes
import com.alorma.contactnotes.domain.notes.Note
import com.jakewharton.rxrelay2.Relay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ListNotesViewModel(private val listContactNotes: ListContactNotes,
                         private val addContactNote: AddContactNote) : BaseViewModel() {

    fun subscribeLoadNotes(lifecycleRelay: Relay<Lifecycle.Event>,
                           contactRelay: Relay<String>,
                           consumer: Consumer<Either<Throwable, List<Note>>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_START)
                .switchMap { contactRelay }
                .observeOn(Schedulers.io())
                .flatMapSingle { listContactNotes.listSingle(it) }
                .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }

    fun onNoteAdded() {

    }
}