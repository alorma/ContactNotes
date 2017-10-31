package com.alorma.contactnotes.data.contacts.store;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts")
    Flowable<List<ContactEntity>> getAll();

    @Query("SELECT * FROM contacts WHERE id LIKE :id LIMIT 1")
    ContactEntity findById(String id);

    @Insert
    void insertAll(ContactEntity... users);

    @Delete
    void delete(ContactEntity user);
}