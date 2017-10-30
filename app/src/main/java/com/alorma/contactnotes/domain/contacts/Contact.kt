package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.domain.notes.Note
import java.util.*

data class Contact(val id: String = UUID.randomUUID().toString(),
                   val androidId: String,
                   val name: String,
                   val userEmail: String? = null,
                   val userPhone: String? = null,
                   val lookup: String? = null,
                   val notes: List<Note>? = listOf())

fun <T> Contact.map(function: (Contact?) -> T): T {
    return function.invoke(this)
}
