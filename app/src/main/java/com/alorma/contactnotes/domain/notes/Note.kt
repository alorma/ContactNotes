package com.alorma.contactnotes.domain.notes

import java.util.*

data class Note(val id: String?,
                val text: String,
                val date: Date? = Date())