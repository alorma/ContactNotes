package com.alorma.contactnotes.data.contacts.store

import android.arch.persistence.room.*

@Dao
interface ContactDao {
    @get:Query("SELECT * FROM contacts")
    val all: List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): ContactEntity

    @Query("SELECT * FROM contacts WHERE androidId LIKE :id LIMIT 1")
    fun findByAndroidId(id: String): ContactEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg contactEntity: ContactEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg contactEntity: ContactEntity)

    @Delete
    fun delete(user: ContactEntity)
}