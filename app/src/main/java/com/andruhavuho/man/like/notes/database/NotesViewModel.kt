package com.andruhavuho.man.like.notes.database

import androidx.lifecycle.ViewModel
import com.andruhavuho.man.like.notes.models.UserNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesViewModel(
    private val db: AppDatabase
) : ViewModel() {

    suspend fun updateNote(noteEntity: NoteEntity) {
        withContext(Dispatchers.Default) {
            db.noteDao().updateNote(noteEntity)
        }
    }

    suspend fun getNote(noteId: Long): NoteEntity {
        return withContext(Dispatchers.Default) {
            db.noteDao().getNote(noteId)
        }
    }

    suspend fun getNotes(): MutableList<UserNote> {
        return withContext(Dispatchers.Default) {
            db.noteDao()
                .getAll()
                .map { armyEntity -> armyEntity.toUserNote(armyEntity.noteId) }.toMutableList()
        }
    }

    suspend fun deleteNoteFromDB(noteId: Long) {
        withContext(Dispatchers.Default) {
            db.noteDao().apply {
                delete(getNote(noteId))
            }
        }
    }

    suspend fun returnNoteBackToDB(recoveredNote: UserNote) {
        withContext(Dispatchers.Default) {
            val newEntity = NoteEntity(recoveredNote.id, recoveredNote.name, recoveredNote.description)
            db.noteDao().insert(newEntity)
        }
    }

    suspend fun insertNewEntity(newEntity: NoteEntity): Long {
        return withContext(Dispatchers.Default) {
            db.noteDao().insert(newEntity)
        }
    }
}