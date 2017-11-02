package com.alorma.contactnotes.domain.contacts.create

data class CreateUserForm(val userName: String,
                          var androidId: String? = null,
                          val userEmail: String? = null,
                          val userPhone: String? = null)

inline fun <T> CreateUserForm.map(block: (CreateUserForm) -> T): T = block(this)


