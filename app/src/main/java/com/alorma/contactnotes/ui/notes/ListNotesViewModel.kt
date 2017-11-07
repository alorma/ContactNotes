package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.Lifecycle
import com.alorma.contactnotes.arch.BaseViewModel
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Right
import com.alorma.contactnotes.arch.map
import com.alorma.contactnotes.data.notes.operations.ListContactNotes
import com.alorma.contactnotes.domain.notes.Note
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ListNotesViewModel(private val listContactNotes: ListContactNotes) : BaseViewModel() {

    private val notesSet = mutableSetOf<String>()


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

    fun subscribeNotesSelection(lifecycleRelay: BehaviorRelay<Lifecycle.Event>,
                                notesRelay: Relay<Note>,
                                consumer: Consumer<Either<Throwable, Set<String>>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE)
                .switchMap { notesRelay }
                .observeOn(Schedulers.io())
                .map {
                    it.id?.let {
                        if (notesSet.contains(it)) {
                            notesSet.remove(it)
                        } else {
                            notesSet.add(it)
                        }
                    }
                    Right(notesSet)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }

}