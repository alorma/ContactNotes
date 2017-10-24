package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.alorma.contactnotes.R
import com.alorma.contactnotes.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_CONTACT_ID = "CONTACT_ID"

        fun newInstance(context: Context, contactId: String): Intent {
            return Intent(context, NotesActivity::class.java).apply {
                putExtra(EXTRA_CONTACT_ID, contactId)
            }
        }

        fun getContactId(intent: Intent): String = intent.getStringExtra(EXTRA_CONTACT_ID)
    }

    private lateinit var notesViewModel: ListNotesViewModel
    private lateinit var adapter: NotesAdapter
    private val contactId by lazy { getContactId(intent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        notesViewModel = ViewModelProviders.of(this, NotesViewModelFactory()).get(ListNotesViewModel::class.java)

        createAdapter()
        notesViewModel.load(getContactId(intent)).observe(this, Observer {
            it?.let { adapter.updateList(it) }
        })
    }

    private fun createAdapter() {
        adapter = NotesAdapter {
            openNote(it.id)
        }
        val manager = GridLayoutManager(this, 2)
        recyclerNotes.layoutManager = manager
        recyclerNotes.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.menuActionAddNote -> createNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNote() {
        notesViewModel.onNoteAdded()
        //startActivity(NoteActivity.createIntent(this, contactId))
    }

    private fun openNote(noteId: String) {
        startActivity(NoteActivity.createIntent(this, contactId, noteId))
    }
}