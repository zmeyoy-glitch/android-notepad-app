package com.example.notepad.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * DAO для работы с базой данных заметок (с временными слотами и статусами)
 */
@Dao
interface NoteDao {
    
    /**
     * Получение всех заметок из базы данных (с временными слотами и статусами)
     */
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<NoteEntity>>
    
    /**
     * Сохранение новой или существующей заметки в базе данных (с временным слотом и статусом)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long
    
    /**
     * Обновление существующей заметки в базе данных (с временным слотом и статусом)
     */
    @Update
    suspend fun update(note: NoteEntity)
    
    /**
     * Удаление заметки из базы данных (с временным слотом и статусом)
     */
    @Delete
    suspend fun delete(note: NoteEntity)
    
    /**
     * Получение конкретной заметки по ID из базы данных (с временными слотами и статусами)
     */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId: Long): LiveData<NoteEntity?>
}

/**
 * Расширение для удобного сохранения заметки в базе данных (с временным слотом и статусом)
 */
fun NoteDao.insert(
    note: NoteEntity
): Long {
    return this.insert(note)
}

/**
 * Расширение для удобного обновления заметки в базе данных (с временным слотом и статусом)
 */
fun NoteDao.update(
    note: NoteEntity
): Unit {
    this.update(note)
}

/**
 * Расширение для удобного удаления заметки из базы данных (с временным слотом и статусом)
 */
fun NoteDao.delete(
    note: NoteEntity
): Unit {
    this.delete(note)
}