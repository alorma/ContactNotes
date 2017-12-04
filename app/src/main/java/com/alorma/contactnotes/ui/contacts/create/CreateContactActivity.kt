package com.alorma.contactnotes.ui.contacts.create

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.alorma.contactnotes.R
import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.ExceptionProvider
import com.alorma.contactnotes.arch.fold
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.contacts.create.CreateUserForm
import com.alorma.contactnotes.domain.exception.UserEmailException
import com.alorma.contactnotes.domain.exception.UserNameException
import com.alorma.contactnotes.domain.exception.UserPhoneException
import com.alorma.contactnotes.ui.BaseActivity
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.jakewharton.rxrelay2.BehaviorRelay
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.create_contact_activity.*


class CreateContactActivity : BaseActivity() {

    companion object {
        val REQ_CONTACT_DIRECTORY = 110
        fun createIntent(context: Context): Intent {
            return Intent(context, CreateContactActivity::class.java)
        }

        fun createReturnIntent() = Intent()
    }

    private var contactId: String? = null

    private val contactImportedRelay: BehaviorRelay<Uri> = BehaviorRelay.create()
    private val createContactRelay: BehaviorRelay<CreateUserForm> = BehaviorRelay.create()

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
                .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            Answers.getInstance().logCustom(CustomEvent("Permission").putCustomAttribute("Contacts", "granted"))
                            function()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
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
        contactViewModel.setupCreateContact(lifecycleRelay.lifecycle, createContactRelay, Consumer {
            onContactCreated(it)
        })

        contactViewModel.setupContactImported(lifecycleRelay.lifecycle, contactImportedRelay, Consumer {
            onContactImport(it)
        })
    }

    private fun onContactCreated(it: Either<Throwable, Contact>) {
        it.fold({
            showErrorCreateContact(it)
        }, {
            returnSuccess()
        })
    }

    private fun onContactImport(it: Either<Throwable, Contact>) {
        it.fold({
            showErrorImport()
        }, {
            onContactImportedSuccess(it)
        })
    }

    private fun onContactImportedSuccess(contact: Contact) {
        this.contactId = contact.androidId
        userEditText.editText?.setText(contact.name)
        emailEditText.editText?.setText(contact.userEmail)
        phoneEditText.editText?.setText(contact.userPhone)
    }

    private fun returnSuccess() {
        userEditText.error = null
        emailEditText.error = null
        phoneEditText.error = null

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


        val form = CreateUserForm(userName = userName,
                userEmail = userEmail,
                userPhone = userPhone)

        contactId?.let {
            form.androidId = it
        }

        createContactRelay.accept(form)
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
            contactImportedRelay.accept(it)
        }
    }


    private fun showErrorCreateContact(it: Throwable) {
        when (it) {
            is UserNameException -> {
                userEditText.error = it.reason
            }
            is UserEmailException -> {
                emailEditText.error = it.reason
            }
            is UserPhoneException -> {
                phoneEditText.error = it.reason
            }
            else -> {
                Toast.makeText(this, "Contact not created", Toast.LENGTH_SHORT).show()
                ExceptionProvider().onError(it)
            }
        }
    }

    private fun showErrorImport() {
        Toast.makeText(this, "Contact not imported", Toast.LENGTH_SHORT).show()
    }
}