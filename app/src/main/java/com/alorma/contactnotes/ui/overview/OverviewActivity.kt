package com.alorma.contactnotes.ui.overview

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View.*
import com.alorma.contactnotes.R
import com.alorma.contactnotes.arch.DaggerDiComponent
import com.alorma.contactnotes.ui.contacts.create.CreateContactActivity
import com.alorma.contactnotes.ui.notes.NotesActivity
import kotlinx.android.synthetic.main.activity_overview.*
import javax.inject.Inject

class OverviewActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_CONTACT_REQUEST_CODE: Int = 1234
    }

    private lateinit var viewModel: OverviewViewModel
    private val contactsAdapter: ContactsOverviewAdapter by lazy {
        ContactsOverviewAdapter({
            startActivity(NotesActivity.newInstance(this@OverviewActivity, it.id))
        })
    }

    @Inject
    lateinit var factory: OverViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        DaggerDiComponent.create().inject(this)


        initView()
    }

    private fun initView() {
        recyclerOverview.visibility = VISIBLE
        contactsText.visibility = VISIBLE
        fabAdd.visibility = VISIBLE

        setupFAB()
        setupAdapter()

        viewModel = ViewModelProviders.of(this, factory).get(OverviewViewModel::class.java)
        subscribe()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadContacts()
    }

    private fun setupAdapter() {
        recyclerOverview.layoutManager = GridLayoutManager(this, 2)
        recyclerOverview.adapter = contactsAdapter
        contactsAdapter.clear()
    }

    private fun setupFAB() {
        fabAdd.setOnClickListener({ _ ->
            openContactSelector()
        })
    }

    private fun openContactSelector() {
        startActivityForResult(CreateContactActivity.createIntent(this), CREATE_CONTACT_REQUEST_CODE)
    }

    private fun subscribe() {
        viewModel.contactsLiveData.observe(this, Observer {
            it?.let {
                contactsAdapter.updateItems(it)

                if (contactsAdapter.itemCount == 0) {
                    onContactsEmpty()
                } else {
                    recyclerOverview.visibility = VISIBLE
                    contactsText.visibility = GONE
                }
            }
        })
    }

    private fun onContactsEmpty() {
        recyclerOverview.visibility = INVISIBLE
        contactsText.visibility = VISIBLE

        contactsText.text = "Contacts saved: empty"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == CREATE_CONTACT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.loadContacts()
            }
        }
    }
}