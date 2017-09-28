package com.alorma.contactnotes.ui.overview

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.*
import android.widget.TextView
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.ui.contacts.ContactsActivity
import com.alorma.contactnotes.ui.contacts.ContactsAdapter

class OverviewActivity : AppCompatActivity() {

    companion object {
        private val GET_CONTACT_REQUEST_CODE: Int = 1234
    }

    private lateinit var viewModel: OverviewViewModel
    private lateinit var contactsAdapter: ContactsOverviewAdapter

    private val recyclerOverview: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerOverview) }
    private val textView: TextView by lazy { findViewById<TextView>(R.id.contactsText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        setupFAB()
        setupAdapter()

        viewModel = OverviewInjector.provideOverviewViewModel(this)

        subscribe()

        if (savedInstanceState == null) {
            viewModel.loadContacts()
        }

    }

    private fun setupAdapter() {
        recyclerOverview.layoutManager = GridLayoutManager(this, 2)
        contactsAdapter = ContactsOverviewAdapter({

        })
        recyclerOverview.adapter = contactsAdapter
    }

    private fun setupFAB() {
        val fab: View = findViewById(R.id.fabAdd)
        fab.setOnClickListener({ _ ->
            openContactSelector()
        })
    }

    private fun openContactSelector() {
        startActivityForResult(ContactsActivity.createIntent(this), GET_CONTACT_REQUEST_CODE)
    }

    private fun subscribe() {
        viewModel.getContacts().subscribe(this, {
            if (it.isEmpty()) {
                onContactsEmpty()
            } else {
                onContactLoaded(it)
            }
        }, {

        })
    }

    private fun onContactLoaded(list: List<Contact>) {
        recyclerOverview.visibility = VISIBLE
        textView.visibility = GONE
        contactsAdapter.clear()
        contactsAdapter.addItems(list)
    }

    private fun onContactsEmpty() {
        recyclerOverview.visibility = INVISIBLE
        textView.visibility = VISIBLE

        textView.text = "Contacts saved: empty"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val contactId = ContactsActivity.getResult(data)
            contactId?.let {
                viewModel.insertContact(contactId).subscribe(this, {

                }, {

                })
            }
        }
    }
}