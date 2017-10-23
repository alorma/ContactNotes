package com.alorma.contactnotes.domain.create

data class CreateUserForm(val userName: String,
                          val androidId: String? = null,
                          val userEmail: String? = null,
                          val userPhone: String? =  null,
                          val lookup: String? = null)
