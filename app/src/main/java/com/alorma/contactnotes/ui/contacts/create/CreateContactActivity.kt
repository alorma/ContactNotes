package com.alorma.contactnotes.ui.contacts.create

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.alorma.contactnotes.R
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import kotlinx.android.synthetic.main.create_contact_activity.*


class CreateContactActivity : AppCompatActivity() {

    companion object {
        val REQ_CONTACT_DIRECTORY = 110
        fun createIntent(context: Context): Intent {
            return Intent(context, CreateContactActivity::class.java)
        }

        fun createReturnIntent() = Intent()
    }

    private lateinit var contactViewModel: CreateContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_contact_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = CreateContactViewModelFactory(contentResolver)
        contactViewModel = ViewModelProviders.of(this, factory).get(CreateContactViewModel::class.java)

        subscribe()

        saveButton.setOnClickListener {
            onSaveClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_contact, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> returnCancel()
            R.id.menuActionImportContact -> {
                checkPermissions({
                    openContactPicker()
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissions(function: () -> Unit) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(object : BasePermissionListener() {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Answers.getInstance().logCustom(CustomEvent("Permission").putCustomAttribute("Contacts", "granted"))
                        function()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Answers.getInstance().logCustom(CustomEvent("Permission").putCustomAttribute("Contacts", "denied"))
                        Toast.makeText(this@CreateContactActivity, "You should accept contacts permission", Toast.LENGTH_SHORT).show()
                    }
                }).check()
    }

    private fun openContactPicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.Contacts.CONTENT_TYPE
        startActivityForResult(intent, REQ_CONTACT_DIRECTORY)
    }

    private fun subscribe() {
        contactViewModel.getUsernameValidationError().observe(this, Observer {
            userEditText.error = it
        })
        contactViewModel.getEmailValidationError().observe(this, Observer {
            emailEditText.error = it
        })
        contactViewModel.getPhoneValidationError().observe(this, Observer {
            phoneEditText.error = it
        })
    }

    private fun returnSuccess() {
        setResult(Activity.RESULT_OK, createReturnIntent())
        finish()
    }

    private fun returnCancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun onSaveClick() {
        val userName = userEditText.editText?.text.toString()
        val userEmail = emailEditText.editText?.text.toString()
        val userPhone = phoneEditText.editText?.text.toString()

        contactViewModel.create(userName, userEmail, userPhone).observe(this, Observer {
            it?.let {
                if (it) {
                    userEditText.error = null
                    emailEditText.error = null
                    phoneEditText.error = null
                    returnSuccess()
                } else {
                    Toast.makeText(this@CreateContactActivity, "Error creating", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CONTACT_DIRECTORY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val uri = data?.data
                        onImportUserReceived(uri)
                        Answers.getInstance().logCustom(CustomEvent("Pick").putCustomAttribute("RESULT", "OK"))
                    }
                    else -> {
                        Answers.getInstance().logCustom(CustomEvent("Pick").putCustomAttribute("RESULT", "KO"))
                    }
                }
            }
        }
    }

    private fun onImportUserReceived(uri: Uri?) {
        uri?.let {
            contactViewModel.contactImported(it).observe(this, Observer {
                it?.let {
                    userEditText.editText?.setText(it.name)
                    emailEditText.editText?.setText(it.userEmail)
                    phoneEditText.editText?.setText(it.userPhone)
                }
            })
        }
    }
}