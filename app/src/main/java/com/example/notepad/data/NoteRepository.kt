package com.example.notepad.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с базой данных заметок (с временными слотами и статусами)
 */
class NoteRepository {
    
    private val database = AppDatabase.getInstance(this@androidNotepadAppApplication.applicationContext)
    
    /**
     * Получение всех заметок из базы данных (с временными слотами и статусами)
     */
    fun getAllNotes(): LiveData<List<NoteEntity>> {
        return database.noteDao().getAllNotes()
    }
    
    /**
     * Получение конкретной заметки по ID из базы данных (с временными слотами и статусами)
     */
    fun getNoteById(noteId: Long): LiveData<NoteEntity?> {
        return database.noteDao().getNoteById(noteId)
    }
    
    /**
     * Сохранение новой или существующей заметки в базе данных (с временным слотом и статусом)
     */
    suspend fun saveNote(
        title: String,
        content: String,
        startTime: Long = 1L, // По умолчанию 17:00
        status: Int = 0       // По умолчанию черновик
    ): Long {
        val note = NoteEntity(
            id = 0L,
            title = title.ifEmpty { "Новая заметка" },
            content = content.ifEmpty { "" },
            startTime = startTime,
            status = status
        )
        
        return database.noteDao().insert(note)
    }
    
    /**
     * Обновление существующей заметки в базе данных (с временным слотом и статусом)
     */
    suspend fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        startTime: Long = 1L, // По умолчанию 17:00
        status: Int = 0       // По умолчанию черновик
    ): Unit {
        val note = NoteEntity(
            id = noteId,
            title = title.ifEmpty { "Обновленная заметка" },
            content = content.ifEmpty { "" },
            startTime = startTime,
            status = status
        )
        
        database.noteDao().update(note)
    }
    
    /**
     * Удаление заметки из базы данных (с временным слотом и статусом)
     */
    suspend fun deleteNote(note: NoteEntity): Unit {
        database.noteDao().delete(note)
    }
}

/**
 * Расширение для удобного получения всех заметок из базы данных (с временными слотами и статусами)
 */
fun NoteRepository.getAllNotes(): LiveData<List<NoteEntity>> {
    return this.getAllNotes()
}

/**
 * Расширение для удобного получения конкретной заметки по ID из базы данных (с временными слотами и статусами)
 */
fun NoteRepository.getNoteById(noteId: Long): LiveData<NoteEntity?> {
    return this.getNoteById(noteId)
}

/**
 * Расширение для удобного сохранения новой или существующей заметки в базе данных (с временным слотом и статусом)
 */
suspend fun NoteRepository.saveNote(
    title: String,
    content: String,
    startTime: Long = 1L, // По умолчанию 17:00
    status: Int = 0       // По умолчанию черновик
): Long {
    return this.saveNote(title, content, startTime, status)
}

/**
 * Расширение для удобного обновления существующей заметки в базе данных (с временным слотом и статусом)
 */
suspend fun NoteRepository.updateNote(
    noteId: Long,
    title: String,
    content: String,
    startTime: Long = 1L, // По умолчанию 17:00
    status: Int = 0       // По умолчанию черновик
): Unit {
    this.updateNote(noteId, title, content, startTime, status)
}

/**
 * Расширение для удобного удаления заметки из базы данных (с временным слотом и статусом)
 */
suspend fun NoteRepository.deleteNote(
    note: NoteEntity
): Unit {
    this.deleteNote(note)
}