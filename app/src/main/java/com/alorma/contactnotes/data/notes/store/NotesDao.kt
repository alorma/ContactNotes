package com.alorma.contactnotes.data.notes.store

import android.arch.persistence.room.*

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE contactId LIKE :userId")
    fun findByUserId(userId: String): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg noteEntity: NoteEntity): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg noteEntity: NoteEntity): Int

    @Delete
    fun delete(vararg items: NoteEntity)
}