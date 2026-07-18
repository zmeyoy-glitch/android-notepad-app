package com.example.notepad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Модель заметки для Room базы данных
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Время начала слота (в формате HH:mm)
    val startTime: String,
    
    // Время окончания слота (в формате HH:mm)
    val endTime: String,
    
    // Номер временного слота (1-12)
    val slotNumber: Int,
    
    // Текст заметки
    val content: String = "",
    
    // Статус заметки (draft/published/archived)
    val status: NoteStatus = NoteStatus.DRAFT,
    
    // Дата создания
    val createdAt: Long = System.currentTimeMillis(),
    
    // Дата последнего обновления
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Перечисление статусов заметки
 */
enum class NoteStatus {
    DRAFT,      // Черновик
    PUBLISHED,  // Опубликовано
    ARCHIVED    // Архивировано
}