package com.example.notepad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database для хранения заметок с временными слотами и статусами
 */
@Database(entities = [NoteEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun noteDao(): NoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Получение экземпляра базы данных (с временными слотами и статусами)
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notepad_database"
                )
                    .addMigrations(MIGRATION_1_2) // Миграция с версии 1 на версию 2
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
    
    /**
     * Миграция базы данных с версии 1 на версию 2 (добавление полей startTime и status)
     */
    private val MIGRATION_1_2 by lazy {
        object : Migrations(1, 2) {
            override fun migrate(database: RoomDatabase, oldVersion: Int, newVersion: Int) {
                // Миграция данных из старой таблицы в новую с новыми полями
                val db = database as AppDatabase
                val noteDao = db.noteDao()
                
                // Получение всех заметок из старой версии (без startTime и status)
                val notes = noteDao.getAllNotes().value ?: return
                
                // Обновление существующих заметок с начальными значениями для новых полей
                notes.forEach { note ->
                    noteDao.update(note.copy(startTime = 1, status = 0))
                }
            }
        }
    }
}

/**
 * Расширение для удобного получения экземпляра базы данных (с временными слотами и статусами)
 */
fun AppDatabase.getInstance(
    context: Context
): AppDatabase {
    return this.getInstance(context)
}