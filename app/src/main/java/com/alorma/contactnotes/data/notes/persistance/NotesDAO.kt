package com.alorma.contactnotes.data.notes.persistance

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface NotesDAO {

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: String): Maybe<NoteDBModel>

    @Query("SELECT * FROM notes WHERE userId = :userId")
    fun getNotesByUser(userId: String): Single<List<NoteDBModel>>

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Single<List<NoteDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(note: NoteDBModel)
}