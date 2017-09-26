package com.alorma.contactnotes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.alorma.contactnotes.data.contacts.persistance.ContactDBModel
import com.alorma.contactnotes.data.contacts.persistance.ContactsDAO
import com.alorma.contactnotes.data.notes.persistance.NoteDBModel
import com.alorma.contactnotes.data.notes.persistance.NotesDAO


@Database(entities = arrayOf(ContactDBModel::class, NoteDBModel::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactsDao(): ContactsDAO
    abstract fun notesDao(): NotesDAO

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}