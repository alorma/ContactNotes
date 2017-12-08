package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.alorma.contactnotes.R
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.ExceptionProvider
import com.alorma.contactnotes.arch.fold
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.ui.BaseActivity
import com.alorma.contactnotes.ui.note.NoteActivity
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : BaseActivity() {

    companion object {
        private val EXTRA_CONTACT_ID = "CONTACT_ID"

        fun newInstance(context: Context, contactId: String): Intent {
            return Intent(context, NotesActivity::class.java).apply {
                putExtra(EXTRA_CONTACT_ID, contactId)
            }
        }

        fun getContactId(intent: Intent): String = intent.getStringExtra(EXTRA_CONTACT_ID)

        private val contactRelay: Relay<String> = PublishRelay.create()
        private val selectedNotesRelay: Relay<Note> = PublishRelay.create()

    }

    private lateinit var notesViewModel: ListNotesViewModel
    private lateinit var adapter: NotesAdapter
    private val contactId by lazy { getContactId(intent) }

    private val consumer = Consumer<Either<Throwable, List<Note>>> {
        onNotesLoaded(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        notesViewModel = ViewModelProviders.of(this, NotesViewModelFactory()).get(ListNotesViewModel::class.java)

        createAdapter()
        notesViewModel.subscribeLoadNotes(lifecycleRelay.lifecycle, contactRelay, consumer)

        notesViewModel.subscribeNotesSelection(lifecycleRelay.lifecycle, selectedNotesRelay, Consumer {
            onItemsSelected(it)
        })

        fabDelete.setOnClickListener {
            notesViewModel.deleteSelectedNotes(consumer)
        }
    }

    override fun onStart() {
        super.onStart()
        contactRelay.accept(contactId)
    }

    private fun onNotesLoaded(it: Either<Throwable, List<Note>>) {
        fabDelete.hide()
        it.fold({
            onLoadNotesError(it)
        }, {
            adapter.updateList(it)
        })
    }

    private fun onLoadNotesError(throwable: Throwable) {
        Toast.makeText(this@NotesActivity, "Error loading notes", Toast.LENGTH_SHORT).show()
        ExceptionProvider().onError(throwable)
    }

    private fun onItemsSelected(it: Either<Throwable, Set<String>>) {
        it.fold({}, {
            invalidateOptionsMenu()
            if (it.isNotEmpty()) {
                fabDelete.show()
                fabDelete.isEnabled = true
            } else {
                fabDelete.hide()
                fabDelete.isEnabled = false
            }
            adapter.setSelectedItems(it)
        })
    }

    private fun createAdapter() {
        val callback: (Note) -> Unit = {
            it.id?.let {
                openNote(it)
            }
        }
        val callbackLong: (Note) -> Boolean = {
            selectedNotesRelay.accept(it)
            true
        }
        adapter = NotesAdapter(callback, callbackLong)

        val manager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
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
        startActivity(NoteActivity.createIntent(this, contactId))
    }

    private fun openNote(noteId: String) {
        startActivity(NoteActivity.createIntent(this, contactId, noteId))
    }
}