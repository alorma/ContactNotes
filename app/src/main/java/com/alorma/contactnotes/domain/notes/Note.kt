package com.alorma.contactnotes.domain.notes

import java.util.*

data class Note(val id: String = UUID.randomUUID().toString(),
                val text: String,
                val date: Date? = null)