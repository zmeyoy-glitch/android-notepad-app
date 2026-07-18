package com.example.notepad.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository для работы с базой данных заметок (с временными слотами и статусами)
 */
class NoteRepository {
    
    // Инъекция Room Database
    private val database: AppDatabase
    
    init {
        this.database = AppDatabase.getInstance()
    }
    
    /**
     * Получение всех заметок из базы данных (с временными слотами и статусами)
     */
    fun getAllNotes(): Flow<List<NoteEntity>> {
        return database.noteDao().getAllNotes()
    }
    
    /**
     * Сохранение новой или существующей заметки в базе данных (с временным слотом и статусом)
     */
    suspend fun saveNote(
        title: String,
        content: String,
        startTime: Long, // 1-12 (временной слот)
        status: Int      // 0 - черновик, 1 - опубликовано
    ) {
        database.noteDao().insertOrUpdate(NoteEntity(0L, title, content, startTime, status))
    }
    
    /**
     * Обновление существующей заметки в базе данных (с временным слотом и статусом)
     */
    suspend fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        startTime: Long, // 1-12 (временной слот)
        status: Int      // 0 - черновик, 1 - опубликовано
    ) {
        database.noteDao().insertOrUpdate(NoteEntity(noteId, title, content, startTime, status))
    }
    
    /**
     * Удаление заметки из базы данных (с временным слотом и статусом)
     */
    suspend fun deleteNote(note: NoteEntity) {
        database.noteDao().delete(note)
    }
    
    /**
     * Получение конкретной заметки по ID из базы данных (с временными слотами и статусами)
     */
    fun getNoteById(noteId: Long): Flow<NoteEntity?> {
        return database.noteDao().getNoteById(noteId)
    }
}

/**
 * Расширение для удобного сохранения заметки в базе данных (с временным слотом и статусом)
 */
fun NoteRepository.saveNote(
    title: String,
    content: String,
    startTime: Long, // 1-12 (временной слот)
    status: Int      // 0 - черновик, 1 - опубликовано
): Unit {
    this.saveNote(title, content, startTime, status)
}

/**
 * Расширение для удобного обновления заметки в базе данных (с временным слотом и статусом)
 */
fun NoteRepository.updateNote(
    noteId: Long,
    title: String,
    content: String,
    startTime: Long, // 1-12 (временной слот)
    status: Int      // 0 - черновик, 1 - опубликовано
): Unit {
    this.updateNote(noteId, title, content, startTime, status)
}

/**
 * Расширение для удобного удаления заметки из базы данных (с временным слотом и статусом)
 */
fun NoteRepository.deleteNote(
    note: NoteEntity
): Unit {
    this.deleteNote(note)
}