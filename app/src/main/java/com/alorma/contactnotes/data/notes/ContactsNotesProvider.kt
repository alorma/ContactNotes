package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.BuildConfig
import com.alorma.contactnotes.domain.notes.Note

class ContactsNotesProvider private constructor() {

    private val items = mutableMapOf<String, MutableMap<String, Note>>()

    private object Holder {
        val INSTANCE = ContactsNotesProvider()
    }

    companion object {
        val INSTANCE: ContactsNotesProvider by lazy { Holder.INSTANCE }
    }

    fun add(userId: String, note: Note) {
        if (items[userId] == null) {
            items.put(userId, mutableMapOf())
        }
        items[userId]?.apply {
            put(note.id, note)
        }
    }

    fun list(userId: String): List<Note> {
        val mutableListOf = mutableListOf<Note>()
        items[userId]?.let {
            mutableListOf.addAll(it.values)
        }
        if (mutableListOf.size == 0 && BuildConfig.DEBUG) {
            mutableListOf.add(Note(text = "Fake text"))
        }
        return mutableListOf
    }
}