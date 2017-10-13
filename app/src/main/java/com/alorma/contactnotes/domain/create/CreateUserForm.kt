package com.alorma.contactnotes.domain.create

data class CreateUserForm(val userName: String,
                          val userEmail: String? = null,
                          val userPhone: String? =  null,
                          val lookup: String? = null)
