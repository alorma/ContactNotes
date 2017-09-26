package com.alorma.contactnotes.ui.overview

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.ui.contacts.ContactsActivity

class OverviewActivity : AppCompatActivity() {

    companion object {
        private val GET_CONTACT_REQUEST_CODE: Int = 1234
    }

    private lateinit var viewModel: OverviewViewModel

    private val textView: TextView by lazy { findViewById<TextView>(R.id.contactsText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        setupFAB()

        viewModel = OverviewInjector.provideOverviewViewModel(this)

        subscribe()
        viewModel.loadContacts()
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
        viewModel.contacts.observe(this, Observer<List<Contact>> {
            it?.let {
                if (it.isNotEmpty()) onContactLoaded(it.size) else onContactsEmpty()
            }
        })
        viewModel.error.observe(this, Observer<Throwable> {

        })
        viewModel.contactInserted.observe(this, Observer<Contact> {

        })
    }

    private fun onContactLoaded(size: Int) {
        textView.text = "Contacts saved: $size"
    }

    private fun onContactsEmpty() {
        textView.text = "Contacts saved: empty"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val contactId = ContactsActivity.getResult(data)
            contactId?.let {
                viewModel.insertContact(contactId)
            }
        }
    }
}