package com.alorma.contactnotes.data.notes.persistance

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class NoteDBModel(@PrimaryKey
                       @ColumnInfo(name = "id")
                       val id: String = UUID.randomUUID().toString(),
                       @ColumnInfo(name = "userId")
                       val userId: String,
                       @ColumnInfo(name = "content")
                       val content: String? = null)