package com.alorma.contactnotes.domain.contacts

import com.alorma.contactnotes.domain.notes.Note

data class Contact(val rawId: String, val name: String, val photo: String? = null, val notes: List<Note>? = listOf())