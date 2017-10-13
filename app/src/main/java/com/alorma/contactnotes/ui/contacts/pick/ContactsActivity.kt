package com.alorma.contactnotes.ui.contacts.pick

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.contacts.Contact

class ContactsActivity : AppCompatActivity() {

    companion object {
        private val PERMISSION_CODE: Int = 11
        private val READ_CONTACTS = Manifest.permission.READ_CONTACTS

        private val EXTRA_CONTACT: String = "EXTRA_CONTACT"

        fun createIntent(context: Context): Intent {
            return Intent(context, ContactsActivity::class.java)
        }

        fun getResult(intent: Intent?): String? {
            return intent?.getStringExtra(EXTRA_CONTACT)
        }

        fun createReturnIntent(contact: Contact): Intent {
            val intent = Intent()
            intent.putExtra(EXTRA_CONTACT, contact.id)
            return intent
        }
    }

    private lateinit var viewModel: ContactsViewModel
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        viewModel = ContactsInjection.provideContactsViewModel(this)
        setupRecycler()
        checkPermission()
        subscribe()
    }

    private fun setupRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        contactsAdapter = ContactsAdapter({
            onContactSelected(it)
        })
        recyclerView.adapter = contactsAdapter
    }

    private fun onContactSelected(contact: Contact) {
        setResult(Activity.RESULT_OK, createReturnIntent(contact))
        finish()
    }

    private fun checkPermission() {
        val checkPermission = packageManager.checkPermission(packageName, READ_CONTACTS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(READ_CONTACTS), PERMISSION_CODE)
        } else {
            loadContacts()
        }
    }

    private fun subscribe() {
        viewModel.contacts.observe(this, Observer<List<Contact>> {
            it?.let {
                onContactsLoaded(it)
            }
        })
        viewModel.errorLiveData.observe(this, Observer<Throwable> {
            it?.printStackTrace()
        })
    }

    private fun loadContacts() {
        viewModel.loadContacts()
    }

    private fun onContactsLoaded(contacts: List<Contact>) {
        contactsAdapter.addItems(contacts)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {
            if (permissions.isNotEmpty()
                    && grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            }
        }
    }
}
