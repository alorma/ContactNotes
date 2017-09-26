package com.alorma.contactnotes.data.contacts.persistance

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface ContactsDAO {

    @Query("SELECT * FROM contacts WHERE rawID = :rawID")
    fun getContactById(rawID: String): Maybe<ContactDBModel>

    @Query("SELECT * FROM Contacts")
    fun getContacts(): Flowable<List<ContactDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: ContactDBModel)
}