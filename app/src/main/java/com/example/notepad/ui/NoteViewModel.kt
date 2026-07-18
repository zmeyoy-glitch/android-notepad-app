package com.example.notepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.notepad.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для управления заметками с учетом временных слотов
 */
class NoteViewModel : ViewModel() {
    
    // Состояние заметок (для реактивного обновления UI)
    private val _notes = MutableStateFlow(emptyList<Note>())
    val notes: StateFlow<List<Note>> = _notes
    
    /**
     * Создать новую заметку с выбранным временным слотом
     */
    fun createNote(
        title: String?,
        content: String,
        startTime: Long, // 1-12 для выбора слота
        status: Int = 0 // 0 - черновик, 1 - опубликовано
    ) {
        viewModelScope.launch {
            val newNote = Note(
                id = 0L, // Будет установлен Room при вставке
                title = title.ifEmpty { null },
                content = content,
                startTime = startTime,
                status = status,
                createdAt = System.currentTimeMillis()
            )
            
            // Здесь будет вызов DAO для вставки в базу данных
            // val noteId = noteDao.insert(newNote)
            // _notes.value = _notes.value + newNote
            
            println("Создана новая заметка: $newNote")
        }
    }
    
    /**
     * Обновить существующую заметку с новым временем или контентом
     */
    fun updateNote(
        noteId: Long,
        title: String?,
        content: String,
        startTime: Long? = null, // Если null - время не меняется
        status: Int? = null // Если null - статус не меняется
    ) {
        viewModelScope.launch {
            val existingNote = _notes.value.find { it.id == noteId } ?: return@launch
            
            val updatedNote = Note(
                id = noteId,
                title = title.ifEmpty { existingNote.title },
                content = content,
                startTime = startTime ?: existingNote.startTime,
                status = status ?: existingNote.status,
                createdAt = existingNote.createdAt
            )
            
            // Здесь будет вызов DAO для обновления в базе данных
            // noteDao.update(updatedNote)
            
            println("Обновлена заметка: $updatedNote")
        }
    }
    
    /**
     * Удалить заметку по ID
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            val existingNote = _notes.value.find { it.id == noteId } ?: return@launch
            
            // Здесь будет вызов DAO для удаления из базы данных
            // noteDao.delete(existingNote)
            
            println("Удалена заметка с ID: $noteId")
        }
    }
    
    /**
     * Получить все заметки (для отображения в списке)
     */
    fun getAllNotes() {
        viewModelScope.launch {
            // Здесь будет вызов DAO для получения всех заметок
            // val allNotes = noteDao.getAll()
            // _notes.value = allNotes.sortedByDescending { it.createdAt }
            
            println("Получены все заметки")
        }
    }
    
    /**
     * Получить конкретную заметку по ID (для просмотра)
     */
    fun getNoteById(noteId: Long): Note? {
        return _notes.value.find { it.id == noteId }
    }
}

/**
 * Расширение для работы с временными слотами
 */
fun Int.toTimeSlotText(): String = when (this) {
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

/**
 * Расширение для работы со статусом заметки
 */
fun Int.toStatusText(): String = when (this) {
    0 -> "Черновик"
    1 -> "Опубликовано"
    else -> "Архив"
}