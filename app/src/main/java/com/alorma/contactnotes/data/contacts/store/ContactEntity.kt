package com.alorma.contactnotes.data.contacts.store

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0,
                         @ColumnInfo(name = "androidId") val androidId: String? = null,
                         @ColumnInfo(name = "name") val name: String,
                         @ColumnInfo(name = "email") val userEmail: String? = null,
                         @ColumnInfo(name = "phone") val userPhone: String? = null)