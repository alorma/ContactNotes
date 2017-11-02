package com.alorma.contactnotes.data.contacts.store

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import io.reactivex.Flowable

@Dao
interface ContactDao {
    @get:Query("SELECT * FROM contacts")
    val all: Flowable<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): ContactEntity

    @Insert
    fun insertAll(vararg users: ContactEntity)

    @Delete
    fun delete(user: ContactEntity)
}