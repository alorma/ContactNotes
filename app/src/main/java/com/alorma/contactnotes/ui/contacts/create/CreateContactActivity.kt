package com.alorma.contactnotes.ui.contacts.create

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.contacts.Contact
import kotlinx.android.synthetic.main.create_contact_activity.*

class CreateContactActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_CONTACT: String = "EXTRA_CONTACT"

        fun createIntent(context: Context): Intent {
            return Intent(context, CreateContactActivity::class.java)
        }

        fun getResult(intent: Intent?): String? {
            return intent?.getStringExtra(EXTRA_CONTACT)
        }

        fun createReturnIntent(contact: Contact): Intent {
            val intent = Intent()
            intent.putExtra(EXTRA_CONTACT, contact.rawId)
            return intent
        }
    }

    private lateinit var contactViewModel: CreateContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_contact_activity)

        contactViewModel = ViewModelProviders.of(this, CreateContactViewModelFactory()).get(CreateContactViewModel::class.java)

        subscribe()

        saveButton.setOnClickListener {
            onSaveClick()
        }

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
                    Toast.makeText(this@CreateContactActivity, "Cool!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CreateContactActivity, "Error creating", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onSaveClick() {
        val userName = userEditText.editText?.text.toString()
        val userEmail = emailEditText.editText?.text.toString()
        val userPhone = phoneEditText.editText?.text.toString()

        contactViewModel.create(userName, userEmail, userPhone)
    }

}