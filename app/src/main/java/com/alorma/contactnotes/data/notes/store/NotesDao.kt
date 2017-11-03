package com.alorma.contactnotes.data.notes.store

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE contactId LIKE :userId")
    fun findByUserId(userId: String): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg noteEntity: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg noteEntity: NoteEntity)
}