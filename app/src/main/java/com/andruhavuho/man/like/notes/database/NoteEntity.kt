package com.andruhavuho.man.like.notes.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andruhavuho.man.like.notes.models.UserNote

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,
    var name: String = "",
    var description: String = "",
) {
    fun toUserNote(givenId: Long? = null): UserNote {
        return UserNote(
            id = givenId ?: noteId,
            name = name,
            description = description,
        )
    }
}