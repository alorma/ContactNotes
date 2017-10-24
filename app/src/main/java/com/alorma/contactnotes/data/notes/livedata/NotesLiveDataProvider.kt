package com.alorma.contactnotes.data.notes.livedata

import com.alorma.contactnotes.arch.ListLiveData
import com.alorma.contactnotes.domain.notes.Note

class NotesLiveDataProvider private constructor() {

    private object Holder {
        val INSTANCE = NotesLiveDataProvider()
    }

    companion object {
        val INSTANCE: NotesLiveDataProvider by lazy { Holder.INSTANCE }
    }

    private val map = mutableMapOf<String, ListLiveData<Note>>()

    fun get(key: String): ListLiveData<Note>? {
        if (map[key] == null) {
            map.put(key, ListLiveData())
        }
        return map[key]
    }
}