package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.data.notes.livedata.NotesLiveDataProvider
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

            updateLiveData(userId, values.toList())
        }
    }

    private fun updateLiveData(userId: String, values: List<Note>) {
        val liveData = NotesLiveDataProvider.INSTANCE.get(userId)
        liveData?.apply {
            clear()
            addAll(values.sortedByDescending { it.date })
        }
    }

    fun list(userId: String): List<Note> {
        return items[userId]?.let {
            val items = it.values.toList().sortedByDescending { it.date }
            updateLiveData(userId, items)
            items
        } ?: listOf()
    }
}