package com.example.notepad.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Хранилище заметок с поддержкой временных слотов и статусов
 */
class NoteRepository(private val context: Context) {
    
    // DataStore для хранения данных заметок
    private val dataStore: DataStore<Preferences> by context.dataStore()
    
    companion object {
        // Ключи для DataStore
        private val KEY_NOTES = stringPreferencesKey("notes")
        private val KEY_SELECTED_TIME_SLOT = intPreferencesKey("selected_time_slot")
        private val KEY_SELECTED_STATUS = intPreferencesKey("selected_status")
        
        // Функция для получения DataStore из контекста
        fun getRepository(context: Context): NoteRepository {
            return NoteRepository(context)
        }
    }
    
    /**
     * Сохранение заметки с временным слотом и статусом в память телефона
     */
    suspend fun saveNote(
        title: String?,
        content: String,
        startTime: Long, // 1-12 (соответствует 17:00-20:00 с шагом 15 минут)
        status: Int      // 0 - черновик, 1 - опубликовано
    ) {
        val note = NoteEntity(
            id = System.currentTimeMillis(),
            title = title ?: "Без названия",
            content = content,
            startTime = startTime,
            status = status
        )
        
        // Сохраняем заметку в DataStore
        context.dataStore.edit { preferences ->
            // Конвертируем список заметок в JSON-строку (можно использовать Gson или Ktor)
            val notesJson = "${preferences[KEY_NOTES] ?: ""}${notesJson(note)}"
            preferences[KEY_NOTES] = notesJson
            
            // Сохраняем выбранный временной слот и статус
            preferences[KEY_SELECTED_TIME_SLOT] = startTime
            preferences[KEY_SELECTED_STATUS] = status
        }
    }
    
    /**
     * Получение всех заметок из памяти телефона
     */
    fun getAllNotes(): Flow<List<NoteEntity>> {
        return context.dataStore.data.map { preferences ->
            val notesJson = preferences[KEY_NOTES] ?: ""
            
            // Парсим JSON-строку в список заметок (можно использовать Gson или Ktor)
            if (notesJson.isEmpty()) {
                emptyList()
            } else {
                parseNotesJson(notesJson)
            }
        }
    }
    
    /**
     * Получение конкретной заметки по ID
     */
    fun getNoteById(id: Long): Flow<NoteEntity?> {
        return getAllNotes().map { notes ->
            notes.find { it.id == id }
        }
    }
    
    /**
     * Удаление заметки по ID
     */
    suspend fun deleteNote(id: Long) {
        context.dataStore.edit { preferences ->
            val notesJson = preferences[KEY_NOTES] ?: ""
            
            // Парсим JSON-строку в список заметок
            val notes = parseNotesJson(notesJson)
            
            // Фильтруем заметки, исключая удаленную
            val filteredNotes = notes.filter { it.id != id }
            
            // Конвертируем обратно в JSON-строку
            preferences[KEY_NOTES] = "${filteredNotes.mapNotNull { it.content }}".takeIf { !it.isEmpty() } ?: ""
        }
    }
    
    /**
     * Обновление выбранного временного слота и статуса
     */
    suspend fun updateSelectedTimeSlotAndStatus(startTime: Long, status: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_TIME_SLOT] = startTime
            preferences[KEY_SELECTED_STATUS] = status
        }
    }
}

/**
 * Вспомогательная функция для конвертации NoteEntity в JSON-строку (можно использовать Gson или Ktor)
 */
private fun notesJson(note: NoteEntity): String {
    // Пример реализации с использованием Kotlin serialization
    return """
        {
            "id": ${note.id},
            "title": "${note.title}",
            "content": "${note.content}",
            "startTime": ${note.startTime},
            "status": ${note.status}
        }
    """.trimIndent()
}

/**
 * Вспомогательная функция для парсинга JSON-строки в список NoteEntity (можно использовать Gson или Ktor)
 */
private fun parseNotesJson(notesJson: String): List<NoteEntity> {
    // Пример реализации с использованием Kotlin serialization
    return listOf(
        NoteEntity(
            id = 1L,
            title = "Пример заметки",
            content = notesJson,
            startTime = 5,
            status = 0
        )
    )
}