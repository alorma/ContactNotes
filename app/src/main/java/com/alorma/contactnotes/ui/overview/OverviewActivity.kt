package com.alorma.contactnotes.ui.overview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.View.*
import android.widget.Toast
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.ui.contacts.create.CreateContactActivity
import com.alorma.contactnotes.ui.notes.NotesActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_overview.*

class OverviewActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private val CREATE_CONTACT_REQUEST_CODE: Int = 1234
        private val RC_SIGN_IN = 123
        private val RC_RESOLUTION = 124
    }

    private lateinit var viewModel: OverviewViewModel
    private lateinit var contactsAdapter: ContactsOverviewAdapter

    private val mGoogleApiClient: GoogleApiClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        if (FirebaseAuth.getInstance().currentUser == null) {
            initNoLogged()
        } else {
            initLogged(savedInstanceState)
        }
    }

    private fun initNoLogged() {
        loginButton.visibility = VISIBLE
        recyclerOverview.visibility = GONE
        contactsText.visibility = GONE
        fabAdd.visibility = GONE

        loginButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun initLogged(savedInstanceState: Bundle?) {
        loginButton.visibility = View.GONE
        recyclerOverview.visibility = VISIBLE
        contactsText.visibility = VISIBLE
        fabAdd.visibility = VISIBLE

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
            startActivity(NotesActivity.newInstance(this@OverviewActivity, it.id))
        })
        recyclerOverview.adapter = contactsAdapter
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
        viewModel.getContacts().subscribe(this, {
            if (it.isEmpty()) {
                onContactsEmpty()
            } else {
                onContactLoaded(it)
            }
        }, {
            it.printStackTrace()
        })
    }

    private fun onContactLoaded(list: List<Contact>) {
        recyclerOverview.visibility = VISIBLE
        contactsText.visibility = GONE
        contactsAdapter.clear()
        contactsAdapter.addItems(list)
    }

    private fun onContactsEmpty() {
        recyclerOverview.visibility = INVISIBLE
        contactsText.visibility = VISIBLE

        contactsText.text = "Contacts saved: empty"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else if (requestCode == CREATE_CONTACT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.loadContacts()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        initLogged(null)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@OverviewActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        initNoLogged()
                    }
                })
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        p0.startResolutionForResult(this, RC_RESOLUTION)
    }
}