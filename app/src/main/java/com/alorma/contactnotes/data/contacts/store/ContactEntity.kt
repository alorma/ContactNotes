package com.alorma.contactnotes.data.contacts.store

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

@Entity(tableName = "contacts", primaryKeys = arrayOf("id", "androidId"))
data class ContactEntity(val id: String,
                         @ColumnInfo(name = "androidId") val androidId: String,
                         @ColumnInfo(name = "name") val name: String,
                         @ColumnInfo(name = "email") val userEmail: String? = null,
                         @ColumnInfo(name = "phone") val userPhone: String? = null)

fun <T> ContactEntity.map(function: (ContactEntity?) -> T): T {
    return function.invoke(this)
}
