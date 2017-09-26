package com.alorma.contactnotes.data.contacts.persistance

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "contacts")
data class ContactDBModel(@PrimaryKey
                          @ColumnInfo(name = "rawID")
                          val rawID: String = UUID.randomUUID().toString(),
                          @ColumnInfo(name = "name")
                          val name: String,
                          @ColumnInfo(name = "photo")
                          val photo: String?)