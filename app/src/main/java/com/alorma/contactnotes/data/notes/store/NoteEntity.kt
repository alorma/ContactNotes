package com.alorma.contactnotes.data.notes.store

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "notes")
class NoteEntity(@PrimaryKey val id: String,
                 @ColumnInfo(name = "contactId") val contactId: String,
                 @ColumnInfo(name = "content") val content: String? = null,
                 @ColumnInfo(name = "date") val date: Long)