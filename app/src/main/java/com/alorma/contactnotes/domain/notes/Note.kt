package com.alorma.contactnotes.domain.notes

import java.util.*

data class Note(val id: String? = null,
                val text: String? = null,
                val contactId: String,
                val date: Date? = Date())