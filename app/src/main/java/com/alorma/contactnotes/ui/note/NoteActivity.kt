package com.alorma.contactnotes.ui.note

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.alorma.contactnotes.R
import kotlinx.android.synthetic.main.activity_note_editor.*

class NoteActivity : AppCompatActivity() {

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
    }

    private val contactId: String by lazy { getContactId(intent) }
    private val noteId: String? by lazy { getNoteId(intent) }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteViewModel = ViewModelProviders.of(this, NoteViewModelFactory()).get(NoteViewModel::class.java)

        subscribe()

        if (noteId == null) {
            noteContent.hint = "Write something"
            noteViewModel.newNote()
        } else {
            noteId?.let {
                noteViewModel.loadNote(it)
            }
        }
    }

    private fun subscribe() {
        noteViewModel.getData().observe(this, Observer {
            it?.let {
                noteContent.setText(it.text)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        noteViewModel.saveNote(contactId, noteContent.text.toString())
        super.onStop()
    }

}