package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.alorma.contactnotes.R
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.ExceptionProvider
import com.alorma.contactnotes.arch.fold
import com.alorma.contactnotes.data.notes.operations.NoNoteException
import com.alorma.contactnotes.data.notes.operations.NoSaveException
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.ui.BaseActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_note_editor.*

class NoteActivity : BaseActivity() {

    companion object {
        private val NOTE_ID = "EXTRA_NOTE_ID"
        private val CONTACT_ID = "EXTRA_CONTACT_ID"

        fun createIntent(context: Context, contactId: String) = Intent(context, NoteActivity::class.java).apply {
            putExtra(CONTACT_ID, contactId)
        }

        fun createIntent(context: Context, contactId: String, noteId: String) = createIntent(context, contactId).apply {
            putExtra(NOTE_ID, noteId)
        }

        fun getNoteId(intent: Intent): String? = intent.getStringExtra(NOTE_ID)
        fun getContactId(intent: Intent): String = intent.getStringExtra(CONTACT_ID)

        private val contactRelay: Relay<NoteMetaData> = PublishRelay.create()
    }

    private val contactId: String by lazy { getContactId(intent) }
    private val noteId: String? by lazy { getNoteId(intent) }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteViewModel = ViewModelProviders.of(this, NoteViewModelFactory()).get(NoteViewModel::class.java)

        subscribeLoad()
    }

    override fun onStart() {
        super.onStart()
        contactRelay.accept(NoteMetaData(contactId, noteId))
    }

    private fun subscribeLoad() {
        noteViewModel.subscribeLoadNote(lifecycleRelay.lifecycle, noteId, Consumer {
            onNoteLoaded(it)
        })
    }

    private fun createNote(text: String) {
        noteViewModel.subscribeSaveNote(contactId, noteId,
                text,
                Consumer {
                    it.fold({
                        when (it) {
                            is NoSaveException -> {
                                finish()
                            }
                            else -> {
                                Toast.makeText(this@NoteActivity, "Note not saved", Toast.LENGTH_SHORT).show()
                                ExceptionProvider().onError(it)
                                finish()
                            }
                        }
                    }, {
                        finish()
                    })
                })
    }

    private fun onNoteLoaded(it: Either<Throwable, Note>) {
        it.fold({
            onNoteLoadError(it)
        }, {
            noteContent.setText(it.text)
        })
    }

    private fun onNoteLoadError(it: Throwable) {
        when (it) {
            is NoNoteException -> noteContent.hint = "Write something"
            else -> {
                Toast.makeText(this@NoteActivity, "Error", Toast.LENGTH_SHORT).show()
                ExceptionProvider().onError(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> createNote(noteContent.text.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        createNote(noteContent.text.toString())
    }
}