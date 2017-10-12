package com.alorma.contactnotes.ui.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.alorma.contactnotes.R
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_CONTACT_ID = "CONTACT_ID"

        fun newInstance(context: Context, contactId: String): Intent {
            return Intent(context, NotesActivity::class.java).apply {
                putExtra(EXTRA_CONTACT_ID, contactId)
            }
        }
    }

    private lateinit var notesViewModel: ListNotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        notesViewModel = ViewModelProviders.of(this, NotesViewModelFactory()).get(ListNotesViewModel::class.java)

        createAdapter()
        subscribe()

        if (savedInstanceState == null) {
            notesViewModel.load()
        }
    }

    private fun createAdapter() {
        adapter = NotesAdapter()
        val manager = GridLayoutManager(this, 2)
        recyclerNotes.layoutManager = manager
        recyclerNotes.adapter = adapter
    }

    private fun subscribe() {
        notesViewModel.getData().observe(this, Observer {
            it?.let { adapter.updateList(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.menuActionAddNote -> notesViewModel.createNote()
        }
        return super.onOptionsItemSelected(item)
    }
}