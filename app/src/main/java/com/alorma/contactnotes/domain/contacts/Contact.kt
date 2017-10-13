package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.domain.notes.Note

data class Contact(val id: String,
                   val name: String,
                   val userEmail: String? = null,
                   val userPhone: String? = null,
                   val photo: String? = null,
                   val lookup: String? = null,
                   val notes: List<Note>? = listOf())