package com.alorma.contactnotes.ui.contacts.create

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.alorma.contactnotes.R
import kotlinx.android.synthetic.main.create_contact_activity.*

class CreateContactActivity : AppCompatActivity() {

    companion object {
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

        contactViewModel = ViewModelProviders.of(this, CreateContactViewModelFactory()).get(CreateContactViewModel::class.java)

        subscribe()

        saveButton.setOnClickListener {
            onSaveClick()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> returnCancel()
        }
        return super.onOptionsItemSelected(item)
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

        contactViewModel.getResult().observe(this, Observer {
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

        contactViewModel.create(userName, userEmail, userPhone)
    }

}