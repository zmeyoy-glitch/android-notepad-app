package com.example.notepad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database для хранения заметок
 */
@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    
    abstract fun noteDao(): NoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        
        /**
         * Получение единственного экземпляра базы данных (Singleton pattern)
         */
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notepad_database"
                )
                    .fallbackToDestructiveMigration() // Перезаписать данные при обновлении версии
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * DAO для работы с заметками
 */
interface NoteDao {
    
    /**
     * Вставка новой заметки
     */
    suspend fun insertNote(note: Note): Long
    
    /**
     * Обновление существующей заметки
     */
    suspend fun updateNote(note: Note)
    
    /**
     * Получение всех заметок
     */
    suspend fun getAllNotes(): List<Note>
    
    /**
     * Получение заметки по ID
     */
    suspend fun getNoteById(id: Long): Note?
    
    /**
     * Удаление заметки
     */
    suspend fun deleteNote(id: Long)
    
    /**
     * Поиск заметок по содержимому
     */
    suspend fun searchNotes(query: String): List<Note>
}

/**
 * Расширение для работы с DAO
 */
fun NoteDatabase.noteDao(): NoteDao = object : NoteDao {
    override suspend fun insertNote(note: Note): Long {
        return noteDaoInternal().insert(note)
    }
    
    override suspend fun updateNote(note: Note) {
        noteDaoInternal().update(note)
    }
    
    override suspend fun getAllNotes(): List<Note> {
        return noteDaoInternal().getAll()
    }
    
    override suspend fun getNoteById(id: Long): Note? {
        return noteDaoInternal().getById(id)
    }
    
    override suspend fun deleteNote(id: Long) {
        noteDaoInternal().delete(id)
    }
    
    override suspend fun searchNotes(query: String): List<Note> {
        return noteDaoInternal().search(query)
    }
}

/**
 * Внутренний DAO для Room
 */
private class NoteDaoInternal(
    private val db: NoteDatabase
) : androidx.room.RoomDatabase.NoteDao() {
    
    override suspend fun insert(note: com.example.notepad.data.Note): Long = 
        super.insert(note)
    
    override suspend fun update(note: com.example.notepad.data.Note) = 
        super.update(note)
    
    override suspend fun getAll(): List<com.example.notepad.data.Note> = 
        super.getAll()
    
    override suspend fun getById(id: Long): com.example.notepad.data.Note? = 
        super.getById(id)
    
    override suspend fun delete(id: Long) = 
        super.delete(id)
    
    override suspend fun search(query: String): List<com.example.notepad.data.Note> {
        // Поиск по содержимому заметки
        return db.noteDaoInternal().getAll()
            .filter { note ->
                note.content.contains(query, ignoreCase = true) ||
                    note.startTime.contains(query, ignoreCase = true) ||
                    note.endTime.contains(query, ignoreCase = true)
            }
    }
}