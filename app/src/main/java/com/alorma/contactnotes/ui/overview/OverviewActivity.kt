package com.alorma.contactnotes.ui.overview

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View.*
import android.widget.Toast
import com.alorma.contactnotes.R
import com.alorma.contactnotes.arch.DaggerDiComponent
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.LogExceptionProvider
import com.alorma.contactnotes.arch.fold
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.ui.BaseActivity
import com.alorma.contactnotes.ui.contacts.create.CreateContactActivity
import com.alorma.contactnotes.ui.notes.NotesActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_overview.*
import javax.inject.Inject

class OverviewActivity : BaseActivity() {

    private lateinit var viewModel: OverviewViewModel
    private val contactsAdapter: ContactsOverviewAdapter by lazy {
        ContactsOverviewAdapter({
            it.id?.let {
                startActivity(NotesActivity.newInstance(this@OverviewActivity, it))
            }
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
        startActivity(CreateContactActivity.createIntent(this))
    }

    private fun subscribe() {
        viewModel.subscribeLoadContacts(lifecycleRelay.lifecycle, Consumer {
            onContactLoad(it)
        })
    }

    private fun onContactLoad(either: Either<Throwable, List<Contact>>) {
        either.fold({
            LogExceptionProvider().onError(it)
            Toast.makeText(this@OverviewActivity, "Error loading contacts", Toast.LENGTH_SHORT).show()
        }, {
            if (it.isEmpty()) {
                onContactsEmpty()
            } else {
                recyclerOverview.visibility = VISIBLE
                contactsText.visibility = GONE
                contactsAdapter.updateItems(it)
            }
        })
    }

    private fun onContactsEmpty() {
        recyclerOverview.visibility = INVISIBLE
        contactsText.visibility = VISIBLE

        contactsText.text = "Contacts saved: empty"
    }
}