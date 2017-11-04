package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.Lifecycle
import com.alorma.contactnotes.arch.BaseViewModel
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.data.notes.operations.*
import com.alorma.contactnotes.domain.notes.Note
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class NoteViewModel(private val getNote: GetContactNote,
                    private val addNote: AddContactNote,
                    private val updateNote: UpdateContactNote) : BaseViewModel() {

    fun subscribeLoadNote(lifecycleRelay: Relay<Lifecycle.Event>, noteId: String?, consumer: Consumer<Either<Throwable, Note>>) {
        if (noteId != null) {
            filterState(lifecycleRelay, Lifecycle.Event.ON_CREATE)
                    .observeOn(Schedulers.io())
                    .flatMapSingle { getNote.getSingle(noteId) }
                    .takeUntil(filterState(lifecycleRelay, Lifecycle.Event.ON_DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
        } else {
            consumer.accept(Left(NoNoteException()))
        }
    }

    fun subscribeSaveNote(contactId: String,
                          noteId: String?,
                          text: String,
                          consumer: Consumer<Either<Throwable, Note>>) {
        if (noteId == null && text.isNotEmpty()) {
            addNote.add(text, contactId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
        } else if (noteId != null) {
            updateNote.update(text, noteId, contactId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
        } else {
            consumer.accept(Left(NoSaveException()))
        }
    }
}
