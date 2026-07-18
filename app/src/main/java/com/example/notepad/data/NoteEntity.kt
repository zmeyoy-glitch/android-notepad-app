package com.example.notepad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность заметки с временным слотом и статусом
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    
    // Заголовок заметки (может быть пустым для черновиков)
    val title: String,
    
    // Контент заметки
    val content: String,
    
    // Временной слот (1-12, соответствует 17:00-20:00 с шагом 15 минут)
    // 1 = 17:00-17:15, 2 = 17:15-17:30, ..., 12 = 19:45-20:00
    val startTime: Long,
    
    // Статус заметки (0 - черновик, 1 - опубликовано)
    val status: Int
)

/**
 * Вспомогательные функции для работы с временными слотами и статусами
 */
object NoteHelpers {
    
    /**
     * Конвертация номера временного слота в читаемое время
     */
    fun getTimeSlotText(slotNumber: Long): String {
        return when (slotNumber) {
            1 -> "17:00 - 17:15"
            2 -> "17:15 - 17:30"
            3 -> "17:30 - 17:45"
            4 -> "17:45 - 18:00"
            5 -> "18:00 - 18:15"
            6 -> "18:15 - 18:30"
            7 -> "18:30 - 18:45"
            8 -> "18:45 - 19:00"
            9 -> "19:00 - 19:15"
            10 -> "19:15 - 19:30"
            11 -> "19:30 - 19:45"
            12 -> "19:45 - 20:00"
            else -> "Не выбрано время"
        }
    }
    
    /**
     * Конвертация статуса в читаемое название
     */
    fun getStatusText(status: Int): String {
        return when (status) {
            0 -> "Черновик"
            1 -> "Опубликовано"
            else -> "Архив"
        }
    }
    
    /**
     * Конвертация статуса в цвет для отображения
     */
    fun getStatusColor(status: Int): androidx.compose.ui.graphics.Color {
        return when (status) {
            0 -> androidx.compose.ui.graphics.Color.Gray // Черновик - серый
            1 -> androidx.compose.ui.graphics.Color.Green // Опубликовано - зеленый
            else -> androidx.compose.ui.graphics.Color.Orange // Архив - оранжевый
        }
    }
}