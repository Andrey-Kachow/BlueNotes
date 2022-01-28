package com.andruhavuho.man.like.notes.database

import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg notes: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Update
    fun updateNote(note: NoteEntity)

    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE :noteId = noteId")
    fun getNote(noteId: Long): NoteEntity

}
