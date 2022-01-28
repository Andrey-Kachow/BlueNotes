package com.andruhavuho.man.like.notes.models

data class UserNote(
    val id: Long = 0,
    val name: String = "",
    val description: String = ""
) {
    fun getNameOrDefault(): String {
        if (name == "") {
            return "Новая Заметка"
        }
        return name
    }

    fun getDescriptionOrDefault(): String {
        if (description == "") {
            return "Здесь пока нет описания"
        }
        return description
    }
}
