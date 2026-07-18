package com.example.notepad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность заметки с временными слотами и статусами для Room базы данных
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    
    /**
     * Заголовок заметки (может быть пустым)
     */
    val title: String,
    
    /**
     * Контент заметки (может быть пустым)
     */
    val content: String,
    
    /**
     * Временной слот заметки (1-12, где 1 = 17:00, 12 = 20:00)
     */
    val startTime: Long, // 1-12
    
    /**
     * Статус заметки (0 - черновик, 1 - опубликовано)
     */
    val status: Int      // 0 - черновик, 1 - опубликовано
)

/**
 * Расширение для удобного создания сущности заметки с временным слотом и статусом
 */
fun NoteEntity.copy(
    title: String = this.title,
    content: String = this.content,
    startTime: Long = this.startTime,
    status: Int = this.status
): NoteEntity {
    return NoteEntity(id, title, content, startTime, status)
}

/**
 * Расширение для удобного создания сущности заметки с временным слотом и статусом (без ID)
 */
fun NoteEntity.copy(
    id: Long = this.id,
    title: String = this.title,
    content: String = this.content,
    startTime: Long = this.startTime,
    status: Int = this.status
): NoteEntity {
    return NoteEntity(id, title, content, startTime, status)
}