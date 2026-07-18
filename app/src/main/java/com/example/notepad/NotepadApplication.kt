package com.example.notepad

import android.app.Application
import androidx.room.Room
import com.example.notepad.data.NoteDatabase

/**
 * Application class для инициализации базы данных
 */
class NotepadApplication : Application() {
    
    companion object {
        lateinit var database: NoteDatabase
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Инициализация базы данных Room
        database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "notepad_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}