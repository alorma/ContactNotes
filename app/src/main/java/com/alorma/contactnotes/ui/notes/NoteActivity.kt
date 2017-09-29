package com.alorma.contactnotes.ui.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.alorma.contactnotes.R

class NoteActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_CONTACT_ID = "CONTACT_ID"

        fun newInstance(context: Context, contactId: String): Intent {
            return Intent(context, NoteActivity::class.java).apply {
                putExtra(EXTRA_CONTACT_ID, contactId)
            }
        }
    }

    private val noteContent: EditText by lazy { findViewById<EditText>(R.id.noteContent) }

    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        viewModel = NotesInjector.provideOverviewViewModel(this)

        viewModel.getNote().subscribe(this, {
            noteContent.setText(it.text)
        })

        if (savedInstanceState == null) {
            intent?.getStringExtra(EXTRA_CONTACT_ID)?.let { viewModel.loadData(it) }
        }
    }
}