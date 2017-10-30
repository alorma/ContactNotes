package com.alorma.contactnotes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.alorma.contactnotes.data.contacts.store.ContactDao
import com.alorma.contactnotes.data.contacts.store.ContactEntity

@Database(entities = arrayOf(ContactEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}