package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.Lifecycle
import android.util.Log
import com.alorma.contactnotes.arch.*
import com.alorma.contactnotes.data.notes.operations.AddContactNote
import com.alorma.contactnotes.data.notes.operations.GetContactNote
import com.alorma.contactnotes.data.notes.operations.NoNoteException
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.domain.validator.Validator
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class NoteViewModel(private val getNote: GetContactNote,
                    private val addNote: AddContactNote) : BaseViewModel() {


    fun subscribeLoadNote(lifecycleRelay: Relay<Lifecycle.Event>, contactRelay: Relay<NoteMetaData>, consumer: Consumer<Either<Throwable, Note>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE)
                .switchMap { contactRelay }
                .observeOn(Schedulers.io())
                .flatMapSingle { getNote.getSingle(it) }
                .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }

    fun subscribeSaveNote(lifecycleRelay: Relay<Lifecycle.Event>,
                          contactRelay: Relay<NoteMetaData>,
                          textRelay: Observable<String>,
                          noteRelay: Relay<Note>,
                          consumer: Consumer<Either<Throwable, Note>>) {
        filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE)
                .switchMap { contactRelay }
                .flatMap { data -> noteRelay.map { it.copy(contactId = data.contactId) } }
                .flatMap { note -> textRelay.map { note.copy(text = it) } }
                .observeOn(Schedulers.io())
                .flatMap { addNote.add(it).toObservable() }
                .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }
}
