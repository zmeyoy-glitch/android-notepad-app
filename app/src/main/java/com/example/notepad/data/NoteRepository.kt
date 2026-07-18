package com.example.notepad.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository для работы с заметками (с временными слотами и статусами)
 */
@Dao
interface NoteRepository {
    
    /**
     * Получение всех заметок из базы данных (с временными слотами и статусами)
     */
    @Query("SELECT * FROM notes ORDER BY startTime DESC, status DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>
    
    /**
     * Сохранение новой или существующей заметки с временным слотом и статусом
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(
        title: String,
        content: String,
        startTime: Long, // 1-12 (временной слот)
        status: Int      // 0 - черновик, 1 - опубликовано
    )
    
    /**
     * Обновление существующей заметки с временным слотом и статусом
     */
    @Update
    suspend fun updateNote(
        note: NoteEntity
    ) {
        // Метод принимает объект NoteEntity, который содержит все поля включая startTime и status
    }
    
    /**
     * Удаление заметки из базы данных (с временным слотом и статусом)
     */
    @Delete
    suspend fun deleteNote(note: NoteEntity)
    
    /**
     * Получение конкретной заметки по ID (с временными слотами и статусами)
     */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId: Long): Flow<NoteEntity?>
}

/**
 * Расширение для удобного обновления заметки с временным слотом и статусом
 */
fun NoteRepository.updateNote(
    noteId: Long,
    title: String,
    content: String,
    startTime: Long, // 1-12 (временной слот)
    status: Int      // 0 - черновик, 1 - опубликовано
): suspend Unit {
    val existingNote = getNoteById(noteId).firstOrNull() ?: return
    
    val updatedNote = existingNote.copy(
        title = title.ifEmpty { "Без названия" },
        content = content,
        startTime = startTime,
        status = status
    )
    
    this.updateNote(updatedNote)
}

/**
 * Расширение для удобного сохранения новой заметки с временным слотом и статусом
 */
fun NoteRepository.saveNote(
    title: String,
    content: String,
    startTime: Long, // 1-12 (временной слот)
    status: Int      // 0 - черновик, 1 - опубликовано
): suspend Unit {
    val newNote = NoteEntity(
        id = 0L, // ID будет сгенерирован автоматически
        title = title.ifEmpty { "Без названия" },
        content = content,
        startTime = startTime,
        status = status
    )
    
    this.saveNote(
        title = newNote.title,
        content = newNote.content,
        startTime = newNote.startTime,
        status = newNote.status
    )
}