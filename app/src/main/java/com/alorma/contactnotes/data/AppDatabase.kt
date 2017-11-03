package com.alorma.contactnotes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.alorma.contactnotes.data.contacts.store.ContactDao
import com.alorma.contactnotes.data.contacts.store.ContactEntity
import com.alorma.contactnotes.data.notes.store.NoteEntity
import com.alorma.contactnotes.data.notes.store.NotesDao

@Database(entities = arrayOf(ContactEntity::class, NoteEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun notesDao(): NotesDao
}