package com.alorma.contactnotes.data.notes.store

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "notes")
class NoteEntity(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                 @ColumnInfo(name = "contactId") var contactId: String,
                 @ColumnInfo(name = "content") var content: String = "",
                 @ColumnInfo(name = "date") var date: Long = System.currentTimeMillis())