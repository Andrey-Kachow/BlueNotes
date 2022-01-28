package com.andruhavuho.man.like.notes

import android.app.Application
import androidx.room.Room
import com.andruhavuho.man.like.notes.database.AppDatabase
import com.andruhavuho.man.like.notes.database.NotesViewModel

class App : Application() {

    lateinit var vm: NotesViewModel

    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database"
        ).fallbackToDestructiveMigration()
//            .allowMainThreadQueries()
            .build()
        vm = NotesViewModel(db)
    }
}